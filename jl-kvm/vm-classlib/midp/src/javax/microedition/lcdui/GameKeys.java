/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import jlkvm.internal.Device;

/** Package-private: the key code -> game action mapping of Canvas.getGameAction, shared with CustomItem (profile key codes). */
final class GameKeys {
    private GameKeys() {
    }

    static int gameAction(int keyCode) {
        if (keyCode == Device.key0(Device.KEY_UP) || keyCode == Canvas.KEY_NUM2) return Canvas.UP;
        if (keyCode == Device.key0(Device.KEY_DOWN) || keyCode == Canvas.KEY_NUM8) return Canvas.DOWN;
        if (keyCode == Device.key0(Device.KEY_LEFT) || keyCode == Canvas.KEY_NUM4) return Canvas.LEFT;
        if (keyCode == Device.key0(Device.KEY_RIGHT) || keyCode == Canvas.KEY_NUM6) return Canvas.RIGHT;
        if (keyCode == Device.key0(Device.KEY_SELECT) || keyCode == Canvas.KEY_NUM5) return Canvas.FIRE;
        if (keyCode == Canvas.KEY_NUM1) return Canvas.GAME_A;
        if (keyCode == Canvas.KEY_NUM3) return Canvas.GAME_B;
        if (keyCode == Canvas.KEY_NUM7) return Canvas.GAME_C;
        if (keyCode == Canvas.KEY_NUM9) return Canvas.GAME_D;
        if (keyCode == Canvas.KEY_NUM0 || keyCode == Canvas.KEY_STAR || keyCode == Canvas.KEY_POUND || (keyCode >= -11 && keyCode <= -1)) return 0;
        throw new IllegalArgumentException("keyCode " + keyCode);
    }
}
