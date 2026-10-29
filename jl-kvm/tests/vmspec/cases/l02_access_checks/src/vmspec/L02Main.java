package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * Access control and link-time errors (JVMS 2nd ed. 5.4.3 / 5.4.4 and the invoke*, get/putfield rules). The scenarios are the
 * generated classes vmspec.l02b.C0..C31 (tests/vmspec/gen/RawGen.java), each running one instruction sequence. A VM raised
 * error that CLDC does not have is thrown as java.lang.Error with the JVMS name at the start of the message
 * (docs/error-mapping.md); kind() turns both forms into the JVMS simple name so the same expected.txt fits every engine.
 */
public class L02Main extends MIDlet {
    static final String[] LABEL = {
        "invokestatic public", "invokestatic package-private from other package", "invokestatic private", "invokestatic protected, not a subclass",
        "invokestatic protected, subclass", "getstatic public", "getstatic package-private", "getstatic private",
        "getstatic protected, not a subclass", "getstatic protected, subclass", "invokestatic method of package-private class",
        "putstatic final field of another class", "getstatic final field", "invokevirtual of a static method",
        "getstatic of an instance field", "getfield of a static field", "invokestatic of an instance method", "getstatic missing field",
        "invokestatic missing method", "invokespecial <init> not declared in the named class (inherited only)",
        "invokevirtual with a Methodref naming an interface", "invokeinterface on an object that does not implement it",
        "invokeinterface ok", "new abstract class", "new interface", "new class whose superclass is package-private",
        "instanceof null with missing class", "checkcast null with missing class", "instanceof non-null with missing class",
        "anewarray of missing class", "invokevirtual of an abstract method (AbstractMethodError)",
        "invokeinterface selecting a non-public method"
    };

    static void p(String s) { System.out.println("> " + s); }

    static String kind(Throwable t) {
        String n = t.getClass().getName();
        if (n.equals("java.lang.Error")) {
            String m = t.getMessage();
            int c = m == null ? -1 : m.indexOf(':');
            if (c > 0) return m.substring(0, c);
        }
        return n.substring(n.lastIndexOf('.') + 1);
    }

    protected void startApp() {
        for (int i = 0; i < LABEL.length; i++) {
            String r;
            try {
                ((Runnable) Class.forName("vmspec.l02b.C" + i).newInstance()).run();
                r = "ok";
            } catch (Throwable t) {
                r = kind(t);
                System.out.println("# " + i + " class=" + t.getClass().getName());
            }
            p(i + " " + r);
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
