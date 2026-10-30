package jlkvm.host.android;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jlkvm.host.ui.ImageSnap;
import jlkvm.host.ui.ScreenHost;
import jlkvm.host.ui.ScreenModel;
import jlkvm.host.ui.ScreenPlan;
import jlkvm.host.ui.UserActions;

/**
 * {@link ScreenHost} with real Android widgets (PLAN T35c, docs/midp.md section 6): Form, List, TextBox and Alert of the guest
 * become views built from a {@link ScreenPlan}. {@link #screen} runs on the vm-thread: it only builds the plan (copying the images)
 * and posts to the UI thread (R7); every user action goes through {@link UserActions} as one {@code HostEvents.UI_*} event for the guest
 * event loop (D13) - this class never calls guest code. When the guest sends the same screen again (it re-sends the whole model after every
 * change) the existing widgets are updated in place, so the keyboard and the caret stay. Layout is a vertical stack (MIDP leaves the
 * layout to the implementation); MIDP "flow" layouts are approximated. Not tested on a device (UNVERIFIED, V16); the decisions about what
 * is shown live in the JVM-tested {@link ScreenPlan}.
 */
public final class AndroidScreenHost implements ScreenHost {
    /** The Activity side: where the panel is shown (UI thread only). */
    public interface Presenter {
        /** The guest made a Canvas current: show the canvas surface. */
        void showCanvas();

        /** Shows {@code panel} instead of the canvas, with the screen title. */
        void showPanel(View panel, String title);
    }

    private interface Binder {
        void update(ScreenPlan.Row r);
    }

    private final Context context;
    private final Presenter presenter;
    private final Handler ui = new Handler(Looper.getMainLooper());
    private final float density;
    private volatile UserActions actions;

    // UI thread state
    private ScreenPlan shown;
    private LinearLayout panel;
    private ScrollView scroll;
    private List<Binder> binders = new ArrayList<>();
    private AlertDialog dialog;
    private boolean updating;

    public AndroidScreenHost(Context context, Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        this.density = context.getResources().getDisplayMetrics().density;
    }

    /** Connects the user actions to the VM ({@code VmRunner::post}); call before the VM starts. */
    public void bind(UserActions.Sink sink) { actions = new UserActions(sink); }

    // ------------------------------------------------------------------ ScreenHost (vm-thread)

    @Override
    public void screen(ScreenModel m) {
        final ScreenPlan p = ScreenPlan.of(m);
        ui.post(new Runnable() { @Override public void run() { show(p); } });
    }

    @Override
    public String text(int id) {
        UserActions a = actions;
        return a == null ? null : a.text(id);
    }

    @Override
    public void traverseResult(int itemId, int dir, boolean inside, int x, int y, int w, int h) {
        // CustomItem traversal is driven by touch only on a touch screen: nothing to answer
    }

    // ------------------------------------------------------------------ Activity entry points (UI thread)

    /** The back key: runs the back-like command of the screen. @return true when it was handled. */
    public boolean onBack() {
        ScreenPlan p = shown;
        if (p == null) return false;
        UserActions a = actions;
        if (p.kind == ScreenModel.ALERT && p.alert != null) {
            if (dialog == null || a == null) return false;
            ScreenPlan.Cmd c = p.alert.dismiss != null ? p.alert.dismiss : p.alert.buttons.isEmpty() ? null : p.alert.buttons.get(0);
            if (c == null) return false;
            a.command(p.screenId, c.id);
            return true;
        }
        if (p.kind == ScreenModel.CANVAS || p.bar.back == null || a == null) return false;
        a.command(p.screenId, p.bar.back.id);
        return true;
    }

    /** True while a Form / List / TextBox panel (not the canvas) is showing. */
    public boolean isPanelShowing() { return shown != null && shown.kind != ScreenModel.CANVAS && shown.kind != ScreenModel.ALERT; }

    // ------------------------------------------------------------------ building

    private void show(ScreenPlan p) {
        if (dialog != null) {
            try { dialog.dismiss(); } catch (RuntimeException ignored) { }
            dialog = null;
        }
        ScreenPlan old = shown;
        if (p.kind == ScreenModel.CANVAS) {
            shown = p;
            panel = null;
            presenter.showCanvas();
            return;
        }
        if (p.kind == ScreenModel.ALERT) {
            shown = p;
            showAlert(p);
            return;
        }
        if (old != null && panel != null && old.kind == p.kind && old.screenId == p.screenId && old.sameStructure(p)) {
            shown = p;
            updating = true;
            try {
                int i = 0;
                for (Binder b : binders) {
                    ScreenPlan.Row r = p.kind == ScreenModel.TEXTBOX ? p.textBox : p.rows.get(i++);
                    b.update(r);
                }
            } finally {
                updating = false;
            }
            return;
        }
        int keepScroll = scroll != null && old != null && old.screenId == p.screenId ? scroll.getScrollY() : 0;
        shown = p;
        buildPanel(p);
        if (keepScroll > 0) {
            final int y = keepScroll;
            scroll.post(new Runnable() { @Override public void run() { scroll.scrollTo(0, y); } });
        }
        presenter.showPanel(panel, p.title);
    }

    private void buildPanel(ScreenPlan p) {
        binders = new ArrayList<>();
        panel = new LinearLayout(context);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundColor(themeColor(android.R.attr.colorBackground, 0xFF000000));
        if (p.ticker != null && p.ticker.length() > 0) {
            TextView t = new TextView(context);
            t.setText(p.ticker);
            t.setSingleLine(true);
            t.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            t.setMarqueeRepeatLimit(-1);
            t.setSelected(true);
            t.setPadding(dp(8), dp(4), dp(8), dp(4));
            t.setBackgroundColor(0x33888888);
            panel.addView(t, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        scroll = new ScrollView(context);
        LinearLayout body = new LinearLayout(context);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(8), dp(8), dp(8), dp(8));
        scroll.addView(body, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        panel.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        if (p.kind == ScreenModel.FORM) {
            View focus = null;
            for (ScreenPlan.Row r : p.rows) {
                View v = rowView(p, r, r.id);
                body.addView(v, rowParams(r));
                if (r.id == p.focusItem) focus = v;
            }
            if (focus != null) {
                final View f = focus;
                scroll.post(new Runnable() { @Override public void run() { scroll.scrollTo(0, f.getTop()); } });
            }
        } else if (p.kind == ScreenModel.LIST) {
            body.addView(listView(p), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else if (p.kind == ScreenModel.TEXTBOX) {
            EditText e = editText(p.textBox, p.textBox.id, true);
            body.addView(e, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binders.add(textBinder(e));
        }
        if (!p.bar.isEmpty()) panel.addView(commandBar(p), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private LinearLayout.LayoutParams rowParams(ScreenPlan.Row r) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(r.expandH ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(r.newlineBefore ? 10 : 4);
        lp.bottomMargin = dp(r.newlineAfter ? 6 : 2);
        lp.gravity = r.gravity == ScreenPlan.G_RIGHT ? Gravity.END : r.gravity == ScreenPlan.G_CENTER ? Gravity.CENTER_HORIZONTAL : Gravity.START;
        return lp;
    }

    private View commandBar(final ScreenPlan p) {
        LinearLayout bar = new LinearLayout(context);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setBackgroundColor(0x33888888);
        bar.setPadding(dp(4), dp(2), dp(4), dp(2));
        if (p.bar.left != null || !p.bar.menu.isEmpty()) {
            final Button b = new Button(context);
            b.setAllCaps(false);
            if (!p.bar.menu.isEmpty()) {
                b.setText(MENU_LABEL);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) { commandMenu(b, p.bar.menu, p.screenId, false, 0); }
                });
            } else {
                final ScreenPlan.Cmd c = p.bar.left;
                b.setText(c.label);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) { UserActions a = actions; if (a != null) a.command(p.screenId, c.id); }
                });
            }
            bar.addView(b, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        } else {
            bar.addView(new View(context), new LinearLayout.LayoutParams(0, 1, 1f));
        }
        if (p.bar.right != null) {
            final ScreenPlan.Cmd c = p.bar.right;
            Button b = new Button(context);
            b.setAllCaps(false);
            b.setText(c.label);
            b.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { UserActions a = actions; if (a != null) a.command(p.screenId, c.id); }
            });
            bar.addView(b, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        } else {
            bar.addView(new View(context), new LinearLayout.LayoutParams(0, 1, 1f));
        }
        return bar;
    }

    private static final String MENU_LABEL = "Menu";

    /** A popup of commands; {@code item} selects UI_ITEM_COMMAND with the item id {@code id}, else UI_COMMAND with the screen id. */
    private void commandMenu(View anchor, final List<ScreenPlan.Cmd> cmds, final int id, final boolean item, int unused) {
        PopupMenu m = new PopupMenu(context, anchor);
        for (int i = 0; i < cmds.size(); i++) m.getMenu().add(0, i, i, cmds.get(i).label);
        m.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(android.view.MenuItem mi) {
                UserActions a = actions;
                if (a != null) {
                    ScreenPlan.Cmd c = cmds.get(mi.getItemId());
                    if (item) a.itemCommand(id, c.id); else a.command(id, c.id);
                }
                return true;
            }
        });
        m.show();
    }

    // ------------------------------------------------------------------ rows

    private View rowView(ScreenPlan p, final ScreenPlan.Row r, int eventId) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        if (r.label != null && r.label.length() > 0 && r.kind != ScreenPlan.R_BUTTON) {
            TextView l = new TextView(context);
            l.setText(r.label);
            l.setTypeface(Typeface.DEFAULT_BOLD);
            l.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            box.addView(l);
        }
        View v;
        Binder binder;
        switch (r.kind) {
            case ScreenPlan.R_STRING:
            case ScreenPlan.R_LINK: {
                final TextView t = new TextView(context);
                look(t, r.font);
                t.setText(r.text);
                if (r.kind == ScreenPlan.R_LINK) {
                    t.setPaintFlags(t.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
                    t.setTextColor(0xFF3A7BD5);
                }
                v = t;
                binder = new Binder() { @Override public void update(ScreenPlan.Row n) { t.setText(n.text); } };
                break;
            }
            case ScreenPlan.R_BUTTON: {
                final Button b = new Button(context);
                b.setAllCaps(false);
                if (r.image != null) b.setCompoundDrawablesWithIntrinsicBounds(new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap(r.image)), null, null, null);
                b.setText(r.text != null ? r.text : "");
                v = b;
                binder = new Binder() { @Override public void update(ScreenPlan.Row n) { b.setText(n.text != null ? n.text : ""); } };
                break;
            }
            case ScreenPlan.R_IMAGE: {
                ImageView iv = new ImageView(context);
                if (r.image != null) iv.setImageBitmap(bitmap(r.image));
                if (r.alt != null) iv.setContentDescription(r.alt);
                iv.setAdjustViewBounds(true);
                v = iv;
                binder = null;
                break;
            }
            case ScreenPlan.R_EDIT: {
                EditText e = editText(r, r.id, false);
                v = e;
                binder = textBinder(e);
                break;
            }
            case ScreenPlan.R_DATE: {
                v = dateView(r);
                binder = null;
                break;
            }
            case ScreenPlan.R_GAUGE: {
                v = gaugeView(r);
                final View gv = v;
                binder = new Binder() { @Override public void update(ScreenPlan.Row n) { setGauge(gv, n); } };
                break;
            }
            case ScreenPlan.R_CHOICE: {
                ChoiceView cv = choiceView(r, r.id);
                v = cv.view;
                binder = cv;
                break;
            }
            case ScreenPlan.R_SPACER: {
                View s = new View(context);
                s.setMinimumWidth(dp(r.w));
                s.setMinimumHeight(dp(r.h));
                v = s;
                binder = null;
                break;
            }
            case ScreenPlan.R_CUSTOM:
                v = customView(r);
                binder = null;
                break;
            default:
                v = new View(context);
                binder = null;
        }
        box.addView(v);
        binders.add(binder != null ? binder : new Binder() { @Override public void update(ScreenPlan.Row n) { } });
        itemCommands(box, v, r);
        return box;
    }

    /** Tap = the default command (or the only one), long press = the menu of the item's commands. */
    private void itemCommands(View box, View target, final ScreenPlan.Row r) {
        if (r.commands.isEmpty()) return;
        final View.OnLongClickListener lc = new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) { commandMenu(view, r.commands, r.id, true, 0); return true; }
        };
        box.setOnLongClickListener(lc);
        target.setOnLongClickListener(lc);
        boolean tappable = r.kind == ScreenPlan.R_BUTTON || r.kind == ScreenPlan.R_LINK || r.kind == ScreenPlan.R_IMAGE || r.kind == ScreenPlan.R_STRING;
        if (!tappable) return;
        View.OnClickListener tap = new View.OnClickListener() {
            @Override public void onClick(View view) {
                UserActions a = actions;
                if (a == null) return;
                if (r.defaultCommand != null) a.itemCommand(r.id, r.defaultCommand.id);
                else if (r.commands.size() == 1) a.itemCommand(r.id, r.commands.get(0).id);
                else commandMenu(view, r.commands, r.id, true, 0);
            }
        };
        target.setOnClickListener(tap);
        if (target instanceof TextView && !(target instanceof Button)) target.setClickable(true);
    }

    private void look(TextView t, ScreenPlan.FontLook f) {
        if (f == null) return;
        int style = (f.bold ? Typeface.BOLD : 0) | (f.italic ? Typeface.ITALIC : 0);
        t.setTypeface(f.mono ? Typeface.MONOSPACE : Typeface.DEFAULT, style);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, f.size < 0 ? 13 : f.size > 0 ? 20 : 16);
        if (f.underline) t.setPaintFlags(t.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
    }

    private static int inputType(ScreenPlan.Input in, boolean multiline) {
        int t;
        switch (in.kind) {
            case ScreenPlan.IN_EMAIL: t = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS; break;
            case ScreenPlan.IN_NUMBER: t = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED; break;
            case ScreenPlan.IN_DECIMAL: t = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL; break;
            case ScreenPlan.IN_PHONE: t = InputType.TYPE_CLASS_PHONE; break;
            case ScreenPlan.IN_URL: t = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI; break;
            default:
                t = InputType.TYPE_CLASS_TEXT;
                if (in.capsWords) t |= InputType.TYPE_TEXT_FLAG_CAP_WORDS;
                if (in.capsSentences) t |= InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
                if (multiline && !in.password) t |= InputType.TYPE_TEXT_FLAG_MULTI_LINE;
        }
        if (in.noSuggestions || in.sensitive) t |= InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
        if (in.password) {
            if ((t & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER) t |= InputType.TYPE_NUMBER_VARIATION_PASSWORD;
            else if ((t & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT) t |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }
        return t;
    }

    private EditText editText(ScreenPlan.Row r, final int id, boolean multiline) {
        final EditText e = new EditText(context);
        e.setInputType(inputType(r.input, multiline));
        if (r.input.maxSize > 0) e.setFilters(new InputFilter[] {new InputFilter.LengthFilter(r.input.maxSize)});
        e.setText(r.text == null ? "" : r.text);
        e.setSelection(e.getText().length());
        if (!r.input.editable) {
            e.setEnabled(false);
        }
        e.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) {
                if (updating) return;
                UserActions ua = actions;
                if (ua != null) ua.text(id, s.toString());
            }
        });
        return e;
    }

    private Binder textBinder(final EditText e) {
        return new Binder() {
            @Override public void update(ScreenPlan.Row n) {
                String t = n.text == null ? "" : n.text;
                if (!t.contentEquals(e.getText())) {
                    e.setText(t);
                    e.setSelection(e.getText().length());
                }
            }
        };
    }

    private View gaugeView(final ScreenPlan.Row r) {
        View v;
        if (r.indefinite) {
            ProgressBar pb = new ProgressBar(context);
            pb.setIndeterminate(true);
            v = pb;
        } else if (r.interactive) {
            SeekBar sb = new SeekBar(context);
            sb.setMax(r.max);
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override public void onProgressChanged(SeekBar s, int p, boolean fromUser) {
                    UserActions a = actions;
                    if (fromUser && !updating && a != null) a.gauge(r.id, p);
                }
                @Override public void onStartTrackingTouch(SeekBar s) { }
                @Override public void onStopTrackingTouch(SeekBar s) { }
            });
            v = sb;
        } else {
            ProgressBar pb = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            pb.setMax(r.max);
            v = pb;
        }
        setGauge(v, r);
        return v;
    }

    private void setGauge(View v, ScreenPlan.Row r) {
        if (r.indefinite) {
            v.setVisibility(r.value >= 2 ? View.VISIBLE : View.INVISIBLE);   // CONTINUOUS_RUNNING / INCREMENTAL_UPDATING
        } else if (v instanceof ProgressBar) {
            ((ProgressBar) v).setProgress(r.value);
        }
    }

    private View dateView(final ScreenPlan.Row r) {
        final Button b = new Button(context);
        b.setAllCaps(false);
        final long[] cur = {r.date};
        final boolean[] has = {r.hasDate};
        final Runnable show = new Runnable() {
            @Override public void run() {
                if (!has[0]) { b.setText("--"); return; }
                Date d = new Date(cur[0]);
                b.setText(r.dateMode == ScreenPlan.D_DATE ? DateFormat.getDateInstance().format(d)
                        : r.dateMode == ScreenPlan.D_TIME ? DateFormat.getTimeInstance(DateFormat.SHORT).format(d)
                        : DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(d));
            }
        };
        show.run();
        final Runnable commit = new Runnable() {
            @Override public void run() {
                has[0] = true;
                show.run();
                UserActions a = actions;
                if (a != null) a.date(r.id, cur[0]);
            }
        };
        b.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                c.setTimeInMillis(has[0] ? cur[0] : System.currentTimeMillis());
                if (r.dateMode == ScreenPlan.D_TIME) {
                    pickTime(c, cur, commit);
                } else {
                    new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override public void onDateSet(DatePicker dp, int y, int m, int d) {
                            c.set(y, m, d);
                            cur[0] = c.getTimeInMillis();
                            if (r.dateMode == ScreenPlan.D_DATE_TIME) pickTime(c, cur, commit); else commit.run();
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                has[0] = false;
                show.run();
                UserActions a = actions;
                if (a != null) a.dateCleared(r.id);
                return true;
            }
        });
        return b;
    }

    private void pickTime(final Calendar c, final long[] cur, final Runnable commit) {
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override public void onTimeSet(TimePicker tp, int h, int m) {
                c.set(Calendar.HOUR_OF_DAY, h);
                c.set(Calendar.MINUTE, m);
                cur[0] = c.getTimeInMillis();
                commit.run();
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private View customView(final ScreenPlan.Row r) {
        ImageView iv = new ImageView(context);
        if (r.image != null) iv.setImageBitmap(bitmap(r.image));
        if (r.interactionModes != 0) {
            final int w = Math.max(1, r.w), h = Math.max(1, r.h);
            iv.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View view, MotionEvent ev) {
                    UserActions a = actions;
                    if (a == null) return false;
                    int x = (int) (ev.getX() * w / Math.max(1, view.getWidth()));
                    int y = (int) (ev.getY() * h / Math.max(1, view.getHeight()));
                    int code = ev.getActionMasked() == MotionEvent.ACTION_DOWN ? 32 : ev.getActionMasked() == MotionEvent.ACTION_MOVE ? 128
                            : ev.getActionMasked() == MotionEvent.ACTION_UP ? 64 : 0;
                    if (code == 0) return false;
                    a.custom(r.id, code, UserActions.xy(x, y));
                    return true;
                }
            });
        }
        return iv;
    }

    // ------------------------------------------------------------------ choices

    private final class ChoiceView implements Binder {
        View view;
        final List<CompoundButton> buttons = new ArrayList<>();
        Spinner spinner;
        int selectedSpinner = -1;

        @Override
        public void update(ScreenPlan.Row n) {
            for (int i = 0; i < buttons.size() && i < n.options.size(); i++) {
                boolean want = n.options.get(i).selected;
                if (buttons.get(i).isChecked() != want) buttons.get(i).setChecked(want);
            }
            if (spinner != null) {
                for (int i = 0; i < n.options.size(); i++) if (n.options.get(i).selected && spinner.getSelectedItemPosition() != i) { selectedSpinner = i; spinner.setSelection(i); }
            }
        }
    }

    private ChoiceView choiceView(final ScreenPlan.Row r, final int eventId) {
        final ChoiceView cv = new ChoiceView();
        if (r.widget == ScreenPlan.C_SPINNER) {
            final Spinner sp = new Spinner(context);
            List<String> labels = new ArrayList<>();
            for (ScreenPlan.Option o : r.options) labels.add(o.text == null ? "" : o.text);
            sp.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, labels));
            for (int i = 0; i < r.options.size(); i++) if (r.options.get(i).selected) { sp.setSelection(i); cv.selectedSpinner = i; }
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (updating || pos == cv.selectedSpinner) return;
                    cv.selectedSpinner = pos;
                    UserActions a = actions;
                    if (a != null) a.choice(eventId, pos, true);
                }
                @Override public void onNothingSelected(AdapterView<?> parent) { }
            });
            cv.spinner = sp;
            cv.view = sp;
            return cv;
        }
        final boolean multi = r.widget == ScreenPlan.C_CHECK;
        final LinearLayout group = new LinearLayout(context);
        group.setOrientation(LinearLayout.VERTICAL);
        final RadioGroup rg = multi ? null : new RadioGroup(context);
        for (int i = 0; i < r.options.size(); i++) {
            final int idx = i;
            ScreenPlan.Option o = r.options.get(i);
            final CompoundButton b = multi ? new CheckBox(context) : new RadioButton(context);
            b.setText(o.text == null ? "" : o.text);
            look(b, o.font);
            if (o.image != null) b.setCompoundDrawablesWithIntrinsicBounds(null, null, new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap(o.image)), null);
            b.setChecked(o.selected);
            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton v, boolean checked) {
                    if (updating) return;
                    UserActions a = actions;
                    if (a == null) return;
                    if (multi) a.choice(eventId, idx, checked);
                    else if (checked) a.choice(eventId, idx, true);
                }
            });
            cv.buttons.add(b);
            if (rg != null) rg.addView(b); else group.addView(b);
        }
        cv.view = rg != null ? rg : group;
        return cv;
    }

    private View listView(final ScreenPlan p) {
        if (p.listWidget == ScreenPlan.C_IMPLICIT) {
            LinearLayout l = new LinearLayout(context);
            l.setOrientation(LinearLayout.VERTICAL);
            for (final ScreenPlan.Row r : p.rows) {
                TextView t = new TextView(context);
                t.setText(r.text == null ? "" : r.text);
                look(t, r.font);
                t.setPadding(dp(8), dp(12), dp(8), dp(12));
                t.setGravity(Gravity.CENTER_VERTICAL);
                if (r.image != null) {
                    t.setCompoundDrawablesWithIntrinsicBounds(new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap(r.image)), null, null, null);
                    t.setCompoundDrawablePadding(dp(8));
                }
                TypedValue tv = new TypedValue();
                if (context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, tv, true)) t.setBackgroundResource(tv.resourceId);
                t.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) { UserActions a = actions; if (a != null) a.choice(p.screenId, r.id, true); }
                });
                l.addView(t, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                binders.add(new Binder() { @Override public void update(ScreenPlan.Row n) { } });
            }
            return l;
        }
        // EXCLUSIVE / MULTIPLE / POPUP: one choice widget for the whole list
        ScreenPlan.Row all = new ScreenPlan.Row();
        all.kind = ScreenPlan.R_CHOICE;
        all.widget = p.listWidget;
        for (ScreenPlan.Row r : p.rows) all.options.addAll(r.options);
        final ChoiceView cv = choiceView(all, p.screenId);
        // the list rows are updated one by one by show(): feed each its option
        for (int i = 0; i < p.rows.size(); i++) {
            final int idx = i;
            binders.add(new Binder() {
                @Override public void update(ScreenPlan.Row n) {
                    if (cv.spinner != null) {
                        if (n.options.get(0).selected && cv.spinner.getSelectedItemPosition() != idx) { cv.selectedSpinner = idx; cv.spinner.setSelection(idx); }
                    } else if (idx < cv.buttons.size() && cv.buttons.get(idx).isChecked() != n.options.get(0).selected) {
                        cv.buttons.get(idx).setChecked(n.options.get(0).selected);
                    }
                }
            });
        }
        return cv.view;
    }

    // ------------------------------------------------------------------ alert

    private void showAlert(final ScreenPlan p) {
        final ScreenPlan.AlertPlan a = p.alert;
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        if (p.title != null) b.setTitle(p.title);
        LinearLayout body = new LinearLayout(context);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(16), dp(8), dp(16), dp(8));
        if (a.image != null) {
            ImageView iv = new ImageView(context);
            iv.setImageBitmap(bitmap(a.image));
            body.addView(iv);
        }
        TextView t = new TextView(context);
        t.setText(a.text == null ? "" : a.text);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        body.addView(t);
        if (a.indicator != null) {
            View g = gaugeView(a.indicator);
            body.addView(g);
        }
        b.setView(body);
        List<ScreenPlan.Cmd> bs = a.buttons;
        if (bs.isEmpty()) bs = new ArrayList<>();
        if (bs.size() <= 3) {
            // a lone dismiss command is the plain "OK" button
            if (bs.size() > 0) b.setPositiveButton(bs.get(0).label, alertClick(p, bs.get(0)));
            if (bs.size() > 1) b.setNegativeButton(bs.get(1).label, alertClick(p, bs.get(1)));
            if (bs.size() > 2) b.setNeutralButton(bs.get(2).label, alertClick(p, bs.get(2)));
        } else {
            String[] names = new String[bs.size()];
            for (int i = 0; i < names.length; i++) names[i] = bs.get(i).label;
            final List<ScreenPlan.Cmd> fb = bs;
            b.setItems(names, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int which) { UserActions ua = actions; if (ua != null) ua.command(p.screenId, fb.get(which).id); }
            });
        }
        b.setCancelable(false);
        AlertDialog d = b.create();
        d.setCanceledOnTouchOutside(false);
        dialog = d;
        d.show();
    }

    private DialogInterface.OnClickListener alertClick(final ScreenPlan p, final ScreenPlan.Cmd c) {
        return new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface d, int which) {
                UserActions a = actions;
                if (a != null) a.command(p.screenId, c.id);
            }
        };
    }

    // ------------------------------------------------------------------ helpers

    private int dp(int v) { return Math.round(v * density); }

    private int themeColor(int attr, int fallback) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, tv, true) && tv.type >= TypedValue.TYPE_FIRST_COLOR_INT && tv.type <= TypedValue.TYPE_LAST_COLOR_INT) return tv.data;
        return fallback;
    }

    private static Bitmap bitmap(ImageSnap s) {
        return Bitmap.createBitmap(s.argb, s.w, s.h, Bitmap.Config.ARGB_8888);
    }
}
