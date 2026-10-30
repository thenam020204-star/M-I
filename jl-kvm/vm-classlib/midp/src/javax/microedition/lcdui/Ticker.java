/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class Ticker {
    private String text;

    public Ticker(String str) {
        if (str == null) throw new NullPointerException();
        text = str;
    }

    public void setString(String str) {
        if (str == null) throw new NullPointerException();
        text = str;
        Display.touchCurrent();
    }

    public String getString() {
        return text;
    }
}
