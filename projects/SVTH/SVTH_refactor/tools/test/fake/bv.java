/** bv giả: ghi lại mọi lời gọi đặt âm lượng nhạc nền (thời điểm ns, mức). */
public final class bv {
    public static final java.util.Vector VOL = new java.util.Vector();
    public void b(int v) { synchronized (VOL) { VOL.addElement(new long[]{System.nanoTime(), v}); } }
}
