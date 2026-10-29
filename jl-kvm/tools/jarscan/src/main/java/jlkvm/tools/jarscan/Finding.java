package jlkvm.tools.jarscan;

/** One report line. ERROR findings make a jar NOT J2ME-SAFE. */
public final class Finding {
    public enum Level { ERROR, WARN, INFO }

    public final Level level;
    public final String code;
    public final String where;    // class or class.method
    public final String message;

    public Finding(Level level, String code, String where, String message) {
        this.level = level;
        this.code = code;
        this.where = where;
        this.message = message;
    }
}
