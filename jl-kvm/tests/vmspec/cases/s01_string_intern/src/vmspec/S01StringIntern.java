package vmspec;

import javax.microedition.midlet.MIDlet;

public class S01StringIntern extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String b(boolean v) { return v ? "T" : "F"; }

    static String ab = "ab";
    static String a = "a", bb = "b";
    static final String CA = "a";

    protected void startApp() {
        p("literal == literal " + b("ab" == "ab"));
        p("const concat " + b("a" + "b" == "ab"));
        p("final concat " + b(CA + "b" == "ab"));
        String rt = a + bb;
        p("runtime concat == literal " + b(rt == "ab"));
        p("runtime concat.equals " + b(rt.equals("ab")));
        p("intern " + b(rt.intern() == "ab"));
        String n = new String("ab");
        p("new String == literal " + b(n == "ab"));
        p("new String.intern == literal " + b(n.intern() == ab));
        p("other class literal " + b(Other.get() == "other"));
        p("other class intern " + b(new StringBuffer("oth").append("er").toString().intern() == Other.get()));
        p("empty " + b("" == "".intern()) + b(new String("") != ""));
        notifyDestroyed();
    }

    static class Other {
        static String get() { return "other"; }
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
