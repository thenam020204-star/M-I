package java.lang;

public final class Character {
    public static final char MIN_VALUE = '\u0000';
    public static final char MAX_VALUE = '￿';
    private final char value;
    public Character(char c) { value = c; }
    public char charValue() { return value; }
    public boolean equals(Object o) { return o instanceof Character && ((Character) o).value == value; }
    public int hashCode() { return value; }
    public String toString() { return String.valueOf(value); }
    public static boolean isDigit(char c) { return c >= '0' && c <= '9'; }
    public static boolean isLowerCase(char c) { return c >= 'a' && c <= 'z'; }
    public static boolean isUpperCase(char c) { return c >= 'A' && c <= 'Z'; }
    public static boolean isLetter(char c) { return isLowerCase(c) || isUpperCase(c); }
    public static char toLowerCase(char c) { return isUpperCase(c) ? (char) (c + 32) : c; }
    public static char toUpperCase(char c) { return isLowerCase(c) ? (char) (c - 32) : c; }
    public static int digit(char c, int radix) {
        int d = isDigit(c) ? c - '0' : (c >= 'a' && c <= 'z') ? c - 'a' + 10 : (c >= 'A' && c <= 'Z') ? c - 'A' + 10 : -1;
        return d < radix ? d : -1;
    }
}
