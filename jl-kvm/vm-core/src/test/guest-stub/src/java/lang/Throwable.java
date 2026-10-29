package java.lang;

public class Throwable {
    private String message;
    private int[] trace;
    public Throwable() { fillInStackTrace(); }
    public Throwable(String message) { this.message = message; fillInStackTrace(); }
    public String getMessage() { return message; }
    public String toString() {
        String n = getClass().getName();
        return message != null ? n + ": " + message : n;
    }
    private native void fillInStackTrace();
    private native String traceText();
    public void printStackTrace() {
        System.err.println(toString());
        System.err.print(traceText());
    }
}
