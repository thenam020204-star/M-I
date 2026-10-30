package jlkvm.host.audio;

/** Deterministic tone generator: square-ish sine, 16-bit mono PCM (used by the headless device to give tests something to compare). */
public final class ToneSynth {
    private ToneSynth() {}

    public static final int RATE = 8000;

    /** Frequency of MIDI note n in Hz: 440 * 2^((n - 69) / 12). */
    public static double frequency(int note) {
        return 440.0 * StrictMath.pow(2.0, (note - 69) / 12.0);
    }

    /** @return {@code durationMs * RATE / 1000} samples; volume 0..100 scales the amplitude (full scale 12000). */
    public static short[] render(int note, int durationMs, int volume) {
        int n = (int) ((long) durationMs * RATE / 1000);
        short[] pcm = new short[n];
        double w = 2.0 * Math.PI * frequency(note) / RATE;
        double amp = 12000.0 * Math.max(0, Math.min(100, volume)) / 100.0;
        for (int i = 0; i < n; i++) pcm[i] = (short) Math.round(amp * StrictMath.sin(w * i));
        return pcm;
    }
}
