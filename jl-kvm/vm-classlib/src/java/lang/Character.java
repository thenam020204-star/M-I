/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/**
 * Character properties for Basic Latin, Latin-1, Latin Extended-A, Latin Extended Additional (Vietnamese), Greek and Cyrillic
 * (CLDC requires only Basic Latin and Latin-1; the wider ranges follow Unicode and are UNVERIFIED against Nokia devices).
 */
public final class Character {
    public static final int MIN_RADIX = 2;
    public static final int MAX_RADIX = 36;
    public static final char MIN_VALUE = '\u0000';
    public static final char MAX_VALUE = '￿';

    private final char value;

    public Character(char value) {
        this.value = value;
    }

    public char charValue() {
        return value;
    }

    public int hashCode() {
        return (int) value;
    }

    public boolean equals(Object obj) {
        return obj instanceof Character && ((Character) obj).value == value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    /** First code of each decimal digit block (Unicode Nd), 10 digits each. */
    private static final char[] DIGIT_ZERO = {
        '0', '٠', '۰', '०', '০', '੦', '૦', '୦', '௧', '౦', '೦', '൦',
        '๐', '໐', '༠', '０'
    };

    public static boolean isDigit(char ch) {
        if (ch < 0x80) {
            return ch >= '0' && ch <= '9';
        }
        for (int i = 1; i < DIGIT_ZERO.length; i++) {
            if (ch >= DIGIT_ZERO[i] && ch <= DIGIT_ZERO[i] + 9) {
                return true;
            }
        }
        return false;
    }

    public static int digit(char ch, int radix) {
        if (radix < MIN_RADIX || radix > MAX_RADIX) {
            return -1;
        }
        int v = -1;
        if (ch >= '0' && ch <= '9') {
            v = ch - '0';
        } else if (ch >= 'a' && ch <= 'z') {
            v = ch - 'a' + 10;
        } else if (ch >= 'A' && ch <= 'Z') {
            v = ch - 'A' + 10;
        } else if (ch >= 0x80) {
            if (ch >= 'Ａ' && ch <= 'Ｚ') {
                v = ch - 'Ａ' + 10;
            } else if (ch >= 'ａ' && ch <= 'ｚ') {
                v = ch - 'ａ' + 10;
            } else {
                for (int i = 1; i < DIGIT_ZERO.length; i++) {
                    if (ch >= DIGIT_ZERO[i] && ch <= DIGIT_ZERO[i] + 9) {
                        v = ch - DIGIT_ZERO[i];
                        break;
                    }
                }
            }
        }
        return v >= 0 && v < radix ? v : -1;
    }

    public static boolean isLowerCase(char ch) {
        if (ch < 0x80) {
            return ch >= 'a' && ch <= 'z';
        }
        if (ch == 0xAA || ch == 0xB5 || ch == 0xBA || ch == 0xDF) {
            return true;
        }
        return toUpperCase(ch) != ch || (ch == 0x131 || ch == 0x138 || ch == 0x149 || (ch >= 0x1E96 && ch <= 0x1E9D));
    }

    public static boolean isUpperCase(char ch) {
        if (ch < 0x80) {
            return ch >= 'A' && ch <= 'Z';
        }
        return toLowerCase(ch) != ch || ch == 0x1E9E;
    }

    public static char toLowerCase(char ch) {
        if (ch < 0x80) {
            return ch >= 'A' && ch <= 'Z' ? (char) (ch + 32) : ch;
        }
        if (ch < 0x100) {
            return ch >= 0xC0 && ch <= 0xDE && ch != 0xD7 ? (char) (ch + 32) : ch;
        }
        if (ch < 0x180) {
            if (ch == 0x130) {
                return 'i';
            }
            if (ch == 0x178) {
                return 'ÿ';
            }
            if ((ch >= 0x100 && ch <= 0x137) || (ch >= 0x14A && ch <= 0x177)) {
                return (ch & 1) == 0 ? (char) (ch + 1) : ch;
            }
            if ((ch >= 0x139 && ch <= 0x148) || (ch >= 0x179 && ch <= 0x17E)) {
                return (ch & 1) == 1 ? (char) (ch + 1) : ch;
            }
            return ch;
        }
        if (ch >= 0x1E00 && ch <= 0x1EFF) {
            if (ch == 0x1E9E) {
                return 'ß';
            }
            if ((ch <= 0x1E95 || ch >= 0x1EA0) && (ch & 1) == 0) {
                return (char) (ch + 1);
            }
            return ch;
        }
        if (ch >= 0x391 && ch <= 0x3AB && ch != 0x3A2) {
            return (char) (ch + 32);
        }
        if (ch == 0x386) {
            return 'ά';
        }
        if (ch >= 0x388 && ch <= 0x38A) {
            return (char) (ch + 37);
        }
        if (ch == 0x38C) {
            return 'ό';
        }
        if (ch == 0x38E || ch == 0x38F) {
            return (char) (ch + 63);
        }
        if (ch >= 0x410 && ch <= 0x42F) {
            return (char) (ch + 32);
        }
        if (ch >= 0x400 && ch <= 0x40F) {
            return (char) (ch + 80);
        }
        if (ch >= 0x460 && ch <= 0x481 || ch >= 0x48A && ch <= 0x4BF || ch >= 0x4D0 && ch <= 0x4F9) {
            return (ch & 1) == 0 ? (char) (ch + 1) : ch;
        }
        if (ch >= 0xFF21 && ch <= 0xFF3A) {
            return (char) (ch + 32);
        }
        return ch;
    }

    public static char toUpperCase(char ch) {
        if (ch < 0x80) {
            return ch >= 'a' && ch <= 'z' ? (char) (ch - 32) : ch;
        }
        if (ch < 0x100) {
            if (ch == 0xFF) {
                return 'Ÿ';
            }
            if (ch == 0xB5) {
                return 'Μ';
            }
            return ch >= 0xE0 && ch <= 0xFE && ch != 0xF7 ? (char) (ch - 32) : ch;
        }
        if (ch < 0x180) {
            if (ch == 0x131) {
                return 'I';
            }
            if (ch == 0x17F) {
                return 'S';
            }
            if ((ch >= 0x100 && ch <= 0x137) || (ch >= 0x14A && ch <= 0x177)) {
                return (ch & 1) == 1 && ch != 0x131 ? (char) (ch - 1) : ch;
            }
            if ((ch >= 0x139 && ch <= 0x148) || (ch >= 0x179 && ch <= 0x17E)) {
                return (ch & 1) == 0 ? (char) (ch - 1) : ch;
            }
            return ch;
        }
        if (ch >= 0x1E00 && ch <= 0x1EFF) {
            if (ch == 0x1E9B) {
                return 'Ṡ';
            }
            if ((ch <= 0x1E95 || ch >= 0x1EA0) && (ch & 1) == 1) {
                return (char) (ch - 1);
            }
            return ch;
        }
        if (ch >= 0x3B1 && ch <= 0x3CB && ch != 0x3C2) {
            return (char) (ch - 32);
        }
        if (ch == 0x3C2) {
            return 'Σ';
        }
        if (ch == 0x3AC) {
            return 'Ά';
        }
        if (ch >= 0x3AD && ch <= 0x3AF) {
            return (char) (ch - 37);
        }
        if (ch == 0x3CC) {
            return 'Ό';
        }
        if (ch == 0x3CD || ch == 0x3CE) {
            return (char) (ch - 63);
        }
        if (ch >= 0x430 && ch <= 0x44F) {
            return (char) (ch - 32);
        }
        if (ch >= 0x450 && ch <= 0x45F) {
            return (char) (ch - 80);
        }
        if (ch >= 0x460 && ch <= 0x481 || ch >= 0x48A && ch <= 0x4BF || ch >= 0x4D0 && ch <= 0x4F9) {
            return (ch & 1) == 1 ? (char) (ch - 1) : ch;
        }
        if (ch >= 0xFF41 && ch <= 0xFF5A) {
            return (char) (ch - 32);
        }
        return ch;
    }
}
