/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.media;

import javax.microedition.media.MediaException;

/**
 * Player for recorded / synthesised media (MIDI, WAV, MP3, AMR...) played by the host audio device. The guest keeps the state machine;
 * the data lives in host memory (a "source" staged chunk by chunk, so it never sits in the small guest heap). The end of a clip is
 * learned by a guest thread parked in {@link Audio#await0}: listeners run on a guest thread, the host never calls guest code (D13).
 */
public final class ClipPlayer extends BasePlayer {
    private int source;
    private int handle = -1;
    private long durUs = TIME_UNKNOWN;
    private final boolean midi;

    public ClipPlayer(String contentType, int source, boolean midi) {
        super(contentType);
        this.source = source;
        this.midi = midi;
    }

    private void open() throws MediaException {
        int h = Audio.open0(source);
        if (h <= 0) throw new MediaException("the audio device cannot play " + contentType);
        handle = h;
        long d = Audio.duration0(h);
        durUs = d < 0 ? TIME_UNKNOWN : d * 1000L;
    }

    protected boolean midiCapable() {
        return midi;
    }

    protected void doRealize() throws MediaException {
        open();
    }

    protected void doPrefetch() throws MediaException {
        if (handle < 0) open();
        if (mediaUs > 0) Audio.seek0(handle, mediaUs / 1000L);
        Audio.volume0(handle, effectiveLevel());
    }

    protected void doStart() {
        Audio.volume0(handle, effectiveLevel());
        Audio.seek0(handle, mediaUs / 1000L);
        launch();
    }

    private void launch() {
        final int h = handle;
        final int id = runId;
        Audio.play0(h, loops, id);
        new Thread() {
            public void run() {
                if (Audio.await0(h, id) == 1) endOfMedia(id);
            }
        }.start();
    }

    protected void doStop() {
        mediaUs = Audio.position0(handle) * 1000L;
        Audio.stop0(handle);
    }

    protected void doSeekStarted(long us) {
        Audio.stop0(handle);
        Audio.seek0(handle, us / 1000L);
        launch();
    }

    protected void doSeekStopped(long us) throws MediaException {
        mediaUs = us;
        if (handle >= 0) Audio.seek0(handle, us / 1000L);
    }

    protected void doDeallocate() {
        if (handle >= 0) {
            Audio.close0(handle);
            handle = -1;
        }
    }

    protected void doClose() {
        doDeallocate();
        if (source > 0) {
            Audio.freeSource0(source);
            source = 0;
        }
    }

    protected long durationUs() {
        return durUs;
    }

    protected long currentUs() {
        return handle < 0 ? mediaUs : Audio.position0(handle) * 1000L;
    }

    protected void volumeChanged() {
        if (handle >= 0) Audio.volume0(handle, effectiveLevel());
    }

    public int getRateMilli() {
        return handle < 0 ? 100000 : Audio.rate0(handle, 0);
    }

    public int setRateMilli(int milli) {
        return handle < 0 ? 100000 : Audio.rate0(handle, milli > 0 ? milli : 100000);
    }

    public int minRateMilli() {
        return 10000;
    }

    public int maxRateMilli() {
        return 1000000;
    }

    public int getTempoMilli() {
        return handle < 0 ? 120000 : Audio.tempo0(handle, 0);
    }

    public int setTempoMilli(int milli) {
        return handle < 0 ? 120000 : Audio.tempo0(handle, milli > 0 ? milli : 0);
    }
}
