package jlkvm.host.audio;

/** Plays a block of 16-bit mono PCM (the tone generator's output): AudioTrack on Android. */
public interface ToneOut {
    /** Starts the block now and replaces what still sounds. */
    void play(short[] pcm, int rate);

    void stop();
}
