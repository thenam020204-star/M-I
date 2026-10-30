/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.io;

/**
 * Prints characters in the default encoding (microedition.encoding). Output errors never throw: they set the error flag
 * (checkError). Each print goes to the stream as one write; println appends '\n'.
 */
public class PrintStream extends OutputStream {
    private OutputStream out;
    private boolean trouble;

    public PrintStream(OutputStream out) {
        if (out == null) {
            throw new NullPointerException();
        }
        this.out = out;
    }

    private boolean closed() {
        if (out == null) {
            trouble = true;
            return true;
        }
        return false;
    }

    public void flush() {
        synchronized (this) {
            if (closed()) {
                return;
            }
            try {
                out.flush();
            } catch (IOException x) {
                trouble = true;
            }
        }
    }

    public void close() {
        synchronized (this) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException x) {
                    trouble = true;
                }
                out = null;
            }
        }
    }

    public boolean checkError() {
        if (out != null) {
            flush();
        }
        return trouble;
    }

    protected void setError() {
        trouble = true;
    }

    public void write(int b) {
        synchronized (this) {
            if (closed()) {
                return;
            }
            try {
                out.write(b);
            } catch (IOException x) {
                trouble = true;
            }
        }
    }

    public void write(byte[] buf, int off, int len) {
        synchronized (this) {
            if (closed()) {
                return;
            }
            try {
                out.write(buf, off, len);
            } catch (IOException x) {
                trouble = true;
            }
        }
    }

    private void emit(String s, boolean newline) {
        if (s == null) {
            s = "null";
        }
        if (newline) {
            s = s.concat("\n");
        }
        byte[] b = s.getBytes();
        write(b, 0, b.length);
    }

    public void print(boolean b) {
        emit(b ? "true" : "false", false);
    }

    public void print(char c) {
        emit(String.valueOf(c), false);
    }

    public void print(int i) {
        emit(String.valueOf(i), false);
    }

    public void print(long l) {
        emit(String.valueOf(l), false);
    }

    public void print(float f) {
        emit(String.valueOf(f), false);
    }

    public void print(double d) {
        emit(String.valueOf(d), false);
    }

    public void print(char[] s) {
        emit(new String(s), false);
    }

    public void print(String s) {
        emit(s, false);
    }

    public void print(Object obj) {
        emit(String.valueOf(obj), false);
    }

    public void println() {
        emit("", true);
    }

    public void println(boolean x) {
        emit(x ? "true" : "false", true);
    }

    public void println(char x) {
        emit(String.valueOf(x), true);
    }

    public void println(int x) {
        emit(String.valueOf(x), true);
    }

    public void println(long x) {
        emit(String.valueOf(x), true);
    }

    public void println(float x) {
        emit(String.valueOf(x), true);
    }

    public void println(double x) {
        emit(String.valueOf(x), true);
    }

    public void println(char[] x) {
        emit(new String(x), true);
    }

    public void println(String x) {
        emit(x, true);
    }

    public void println(Object x) {
        emit(String.valueOf(x), true);
    }
}
