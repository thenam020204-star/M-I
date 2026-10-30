/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.media;

import java.util.Vector;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 * State machine, listeners, loop count and controls of a Player (MIDP 2.0 javadoc of javax.microedition.media.Player; the subclasses
 * {@link TonePlayer} - guest-timed tones - and {@link ClipPlayer} - media played by the host). Times are microseconds.
 * Events are queued to the "media-events" guest thread ({@link Events}); the state changes at once, in the calling thread.
 */
public abstract class BasePlayer implements Player {
    public static final String CONTROL_PKG = "javax.microedition.media.control.";
    private static boolean factoryLoaded;
    private static ControlFactory factory;

    protected int state = UNREALIZED;
    protected int loops = 1;
    /** Media time while not STARTED. */
    protected long mediaUs;
    /** Incremented by every start / stop / seek: workers of an older run ignore their results. */
    protected int runId;
    protected final String contentType;
    private PlayerListener[] listeners = new PlayerListener[0];
    private Control[] controls;
    private String[] names;
    private int level = 100;
    private boolean muted;

    protected BasePlayer(String contentType) {
        this.contentType = contentType;
    }

    // ---------------------------------------------------------------- hooks
    protected abstract void doRealize() throws MediaException;

    protected void doPrefetch() throws MediaException {
    }

    /** Starts playing at {@link #mediaUs} (runId was incremented already). */
    protected abstract void doStart();

    /** Stops the sound and stores the media time in mediaUs (runId is incremented by the caller). */
    protected abstract void doStop();

    /** Moves to {@code us} while STARTED (runId incremented by the caller): continues playing from there. */
    protected abstract void doSeekStarted(long us);

    protected void doSeekStopped(long us) throws MediaException {
        mediaUs = us;
    }

    protected void doDeallocate() {
    }

    protected abstract void doClose();

    /** Duration in microseconds, TIME_UNKNOWN when not known. */
    protected abstract long durationUs();

    /** Media time while STARTED. */
    protected abstract long currentUs();

    /** Called after the volume or mute state changed; effective level through {@link #effectiveLevel}. */
    protected void volumeChanged() {
    }

    /** Controls of this player besides VolumeControl (ToneControl...). */
    protected void addOwnControls(Vector cs, Vector ns) {
    }

    /** Whether the JSR-135 layer may add MIDIControl / TempoControl (tone sequences, RTTTL, MIDI). */
    protected boolean midiCapable() {
        return false;
    }

    // ---------------------------------------------------------------- JSR-135 controls (rom-jsr135.jar uses these)
    public int getRateMilli() {
        return 100000;
    }

    public int setRateMilli(int milli) {
        return 100000;
    }

    public int minRateMilli() {
        return 100000;
    }

    public int maxRateMilli() {
        return 100000;
    }

    public int getTempoMilli() {
        return 120000;
    }

    public int setTempoMilli(int milli) {
        return 120000;
    }

    // ---------------------------------------------------------------- Player
    private void notClosed() {
        if (state == CLOSED) throw new IllegalStateException("Player is closed");
    }

    private void realized() {
        if (state == CLOSED) throw new IllegalStateException("Player is closed");
        if (state == UNREALIZED) throw new IllegalStateException("Player is not realized");
    }

    private void post(String event, Object data) {
        Events.post(this, listeners, event, data);
    }

    public synchronized void realize() throws MediaException {
        notClosed();
        if (state != UNREALIZED) return;
        doRealize();
        buildControls();
        state = REALIZED;
    }

    public synchronized void prefetch() throws MediaException {
        notClosed();
        if (state == UNREALIZED) realize();
        if (state == REALIZED) {
            doPrefetch();
            state = PREFETCHED;
        }
    }

    public synchronized void start() throws MediaException {
        notClosed();
        if (state == STARTED) return;
        prefetch();
        long d = durationUs();
        if (d > 0 && mediaUs >= d) mediaUs = 0;   // after the end a new start plays from the beginning (UNVERIFIED)
        runId++;
        state = STARTED;
        doStart();
        post(PlayerListener.STARTED, new Long(mediaUs));
    }

    public synchronized void stop() throws MediaException {
        notClosed();
        if (state != STARTED) return;
        runId++;
        doStop();
        state = PREFETCHED;
        post(PlayerListener.STOPPED, new Long(mediaUs));
    }

    public synchronized void deallocate() {
        notClosed();
        if (state == STARTED) {
            runId++;
            doStop();
            state = PREFETCHED;
            post(PlayerListener.STOPPED, new Long(mediaUs));
        }
        if (state == PREFETCHED) {
            doDeallocate();
            state = REALIZED;
        }
    }

    public synchronized void close() {
        if (state == CLOSED) return;
        if (state == STARTED) {
            runId++;
            doStop();
            state = PREFETCHED;
            post(PlayerListener.STOPPED, new Long(mediaUs));
        }
        doClose();
        state = CLOSED;
        post(PlayerListener.CLOSED, null);
        listeners = new PlayerListener[0];
    }

    /** The worker of run {@code run} reached the end of the media (all loops played). */
    protected synchronized void endOfMedia(int run) {
        if (run != runId || state != STARTED) return;
        runId++;
        state = PREFETCHED;
        long d = durationUs();
        mediaUs = d < 0 ? mediaUs : d;
        post(PlayerListener.END_OF_MEDIA, new Long(mediaUs));
    }

    public synchronized long setMediaTime(long now) throws MediaException {
        realized();
        long d = durationUs();
        if (now < 0) now = 0;
        if (d >= 0 && now > d) now = d;
        if (state == STARTED) {
            runId++;
            mediaUs = now;
            doSeekStarted(now);
        } else {
            doSeekStopped(now);
        }
        return now;
    }

    public synchronized long getMediaTime() {
        notClosed();
        return state == STARTED ? currentUs() : mediaUs;
    }

    public synchronized int getState() {
        return state;
    }

    public synchronized long getDuration() {
        notClosed();
        return state == UNREALIZED ? TIME_UNKNOWN : durationUs();
    }

    public synchronized String getContentType() {
        realized();
        return contentType;
    }

    public synchronized void setLoopCount(int count) {
        notClosed();
        if (state == STARTED) throw new IllegalStateException("Player is started");
        if (count == 0) throw new IllegalArgumentException("loop count 0");
        loops = count < 0 ? -1 : count;
    }

    public synchronized void addPlayerListener(PlayerListener l) {
        notClosed();
        if (l == null) return;
        PlayerListener[] n = new PlayerListener[listeners.length + 1];
        System.arraycopy(listeners, 0, n, 0, listeners.length);
        n[listeners.length] = l;
        listeners = n;
    }

    public synchronized void removePlayerListener(PlayerListener l) {
        notClosed();
        if (l == null) return;
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == l) {
                PlayerListener[] n = new PlayerListener[listeners.length - 1];
                System.arraycopy(listeners, 0, n, 0, i);
                System.arraycopy(listeners, i + 1, n, i, n.length - i);
                listeners = n;
                return;
            }
        }
    }

    // ---------------------------------------------------------------- controls
    private void buildControls() {
        Vector cs = new Vector();
        Vector ns = new Vector();
        cs.addElement(new Vol());
        ns.addElement(CONTROL_PKG + "VolumeControl");
        addOwnControls(cs, ns);
        if (midiCapable()) {
            ControlFactory f = factory();
            if (f != null) f.addControls(this, cs, ns);
        }
        controls = new Control[cs.size()];
        names = new String[cs.size()];
        for (int i = 0; i < controls.length; i++) {
            controls[i] = (Control) cs.elementAt(i);
            names[i] = (String) ns.elementAt(i);
        }
    }

    private static synchronized ControlFactory factory() {
        if (!factoryLoaded) {
            factoryLoaded = true;
            if ("1".equals(Audio.config0(Audio.CFG_JSR135))) {
                try {
                    factory = (ControlFactory) Class.forName("jlkvm.internal.media.ext.Jsr135Controls").newInstance();
                } catch (Exception e) {
                    // the profile declares jsr-135 but the layer is not in the ROM: no extra controls
                } catch (Error e) {
                    // idem
                }
            }
        }
        return factory;
    }

    public synchronized Control[] getControls() {
        realized();
        Control[] r = new Control[controls.length];
        System.arraycopy(controls, 0, r, 0, r.length);
        return r;
    }

    public synchronized Control getControl(String type) {
        realized();
        if (type == null) throw new IllegalArgumentException("null control type");
        String full = type.indexOf('.') < 0 ? CONTROL_PKG + type : type;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(full)) return controls[i];
        }
        return null;
    }

    /** Level 0..100 the sound must have now (0 when muted). */
    public final synchronized int effectiveLevel() {
        return muted ? 0 : level;
    }

    private final class Vol implements VolumeControl {
        public void setMute(boolean mute) {
            synchronized (BasePlayer.this) {
                if (mute == muted) return;
                muted = mute;
                volumeChanged();
                post(PlayerListener.VOLUME_CHANGED, this);
            }
        }

        public boolean isMuted() {
            synchronized (BasePlayer.this) {
                return muted;
            }
        }

        public int setLevel(int l) {
            synchronized (BasePlayer.this) {
                if (l < 0) l = 0;
                if (l > 100) l = 100;
                if (l != level) {
                    level = l;
                    volumeChanged();
                    post(PlayerListener.VOLUME_CHANGED, this);
                }
                return level;
            }
        }

        public int getLevel() {
            synchronized (BasePlayer.this) {
                return level;
            }
        }
    }
}
