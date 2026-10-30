/*
 * jl-kvm guest class library (Nokia UI layer, API per tools/api-sigs/db/s40dp2-nokia-ui.json). Origin: original code written for jl-kvm
 * (PLAN D12, T28); behaviour per the Nokia UI API 1.1 javadoc (S40 DP2.0 SDK 1.1), open points UNVERIFIED (docs/nokia.md). License: Apache-2.0.
 */
package com.nokia.mid.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import jlkvm.internal.Access;

/** Full screen Canvas; the soft keys and the other command keys arrive as plain key events (codes below, Nokia UI API 1.1). */
public abstract class FullCanvas extends Canvas {
    public static final int KEY_SOFTKEY1 = -6;
    public static final int KEY_SOFTKEY2 = -7;
    public static final int KEY_SOFTKEY3 = -5;
    public static final int KEY_SEND = -10;
    public static final int KEY_END = -11;
    public static final int KEY_UP_ARROW = -1;
    public static final int KEY_DOWN_ARROW = -2;
    public static final int KEY_LEFT_ARROW = -3;
    public static final int KEY_RIGHT_ARROW = -4;

    protected FullCanvas() {
        super();
        Access.canvas.initFull(this);
    }

    public void addCommand(Command cmd) {
        throw new IllegalStateException("Commands are not possible in FullCanvas");
    }

    public void setCommandListener(CommandListener l) {
        throw new IllegalStateException("Commands are not possible in FullCanvas");
    }
}
