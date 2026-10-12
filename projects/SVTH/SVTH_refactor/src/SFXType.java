/** Hằng số loại âm thanh chiến đấu (SFX). Không phụ thuộc class game. */
public final class SFXType {
    public static final int HIT = 0;     // đòn trúng
    public static final int MISS = 1;    // đòn hụt / bị né
    public static final int CRIT = 2;    // lớp thêm cho đòn chí mạng (phát cùng HIT)
    public static final int SKILL = 3;   // tung kỹ năng / hiệu ứng trạng thái
    public static final int FIRE = 4;
    public static final int WATER = 5;
    public static final int EARTH = 6;
    public static final int WIND = 7;
    public static final int HEAL = 8;
    public static final int COUNT = 9;

    /** Biến thể theo nhân vật: 0 = quái/sủng vật, 1 = Quinn (thương), 2 = Julie (súng). */
    public static final int VARIANTS = 3;

    /** Ưu tiên (số lớn = quan trọng hơn) dùng khi hết voice. */
    public static int priority(int type) {
        switch (type) {
            case CRIT: return 5;
            case SKILL: case FIRE: case WATER: case EARTH: case WIND: case HEAL: return 3;
            case HIT: return 2;
            default: return 1;
        }
    }

    public static String name(int type) {
        switch (type) {
            case HIT: return "HIT";
            case MISS: return "MISS";
            case CRIT: return "CRIT";
            case SKILL: return "SKILL";
            case FIRE: return "FIRE";
            case WATER: return "WATER";
            case EARTH: return "EARTH";
            case WIND: return "WIND";
            case HEAL: return "HEAL";
            default: return "?";
        }
    }

    private SFXType() {}
}
