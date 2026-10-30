/*
 * jl-kvm guest class library (Nokia UI layer, API per tools/api-sigs/db/s40dp2-nokia-ui.json). Origin: original code written for jl-kvm
 * (PLAN D12, T28); behaviour per the Nokia UI API 1.1 javadoc (S40 DP2.0 SDK 1.1), open points UNVERIFIED (docs/nokia.md). License: Apache-2.0.
 */
package jlkvm.internal.nokia;

import javax.microedition.lcdui.Image;
import jlkvm.internal.Access;
import jlkvm.internal.LcduiAccess;

/** ROM-private: the friend hooks of lcdui.Graphics (installed when the Graphics class is initialised). */
public final class Lcdui {
    private Lcdui() {
    }

    public static LcduiAccess get() {
        LcduiAccess a = Access.lcdui;
        if (a == null) {
            Image.createImage(1, 1).getGraphics();   // initialises Graphics, whose static initialiser installs the hooks
            a = Access.lcdui;
        }
        return a;
    }
}
