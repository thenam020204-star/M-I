package jlkvm.host;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.heap.Heap;
import jlkvm.host.audio.AudioDevice;
import jlkvm.link.VmThrow;
import jlkvm.natives.AsyncBlocker;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * Natives of {@code jlkvm.internal.media.Audio} (PLAN T27): the guest keeps the MMAPI rules, this class puts the {@link AudioDevice}
 * behind them. Clip data is staged here chunk by chunk (never in the guest heap). The end of a clip comes from the device on any
 * thread ({@link AudioDevice.Listener}); it only completes the {@link AsyncBlocker} of the guest thread parked in {@code await0}, so
 * listeners always run on guest threads (D13). docs/midp.md section 10.
 */
public final class AudioNatives {
    private static final String AUDIO = "jlkvm/internal/media/Audio.";
    /** Largest clip taken from a MIDlet. */
    public static final int MAX_CLIP_BYTES = 16 << 20;

    private final DeviceProfile profile;
    private final AudioDevice device;
    private ByteArrayOutputStream stage = new ByteArrayOutputStream();
    private boolean stageOverflow;
    private final Map<Integer, Object[]> sources = new HashMap<Integer, Object[]>();   // id -> {byte[], String}
    private final Map<Integer, Wait> clips = new HashMap<Integer, Wait>();
    private int nextSource = 1;

    private static final class Pending extends AsyncBlocker {
        volatile int result;
        protected void complete(NativeContext c) { c.retInt(result); }
    }

    private static final class Wait {
        int run = -1;
        boolean ended;
        Pending blocker;
    }

    public AudioNatives(DeviceProfile profile, AudioDevice device) {
        this.profile = profile;
        this.device = device;
        device.setListener(new AudioDevice.Listener() { public void ended(int clip) { AudioNatives.this.ended(clip); } });
    }

    private synchronized void ended(int clip) {
        Wait w = clips.get(Integer.valueOf(clip));
        if (w == null || w.run < 0) return;
        if (w.blocker != null) { w.blocker.result = 1; w.blocker.finish(); w.blocker = null; w.run = -1; }
        else w.ended = true;
    }

    private void release(Wait w) {
        if (w.blocker != null) { w.blocker.result = 0; w.blocker.finish(); w.blocker = null; }
        w.run = -1;
        w.ended = false;
    }

    private synchronized Wait waitOf(int clip) {
        Wait w = clips.get(Integer.valueOf(clip));
        if (w == null) throw new VmThrow("java/lang/IllegalStateException", "bad audio clip " + clip);
        return w;
    }

    private static String join(java.util.List<String> l) {
        StringBuilder sb = new StringBuilder();
        for (String s : l) { if (sb.length() > 0) sb.append(','); sb.append(s); }
        return sb.toString();
    }

    public void register(NativeRegistry r) {
        final GuestServices gs = r.services();
        r.register(AUDIO + "config0(I)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            String v;
            switch (c.argInt(0)) {
                case 0: v = join(profile.mediaContentTypes); break;
                case 1: v = join(profile.mediaProtocols); break;
                case 2: v = profile.mediaTone ? "1" : "0"; break;
                default: v = profile.apis.contains("jsr-135") ? "1" : "0";
            }
            c.retRef(gs.newString(v)); } });
        r.register(AUDIO + "tone0(III)V", new NativeMethod() { public void invoke(NativeContext c) {
            device.tone(c.argInt(0), c.argInt(1), c.argInt(2)); } });
        r.register(AUDIO + "silence0()V", new NativeMethod() { public void invoke(NativeContext c) { device.silence(); } });
        r.register(AUDIO + "midiShort0(III)V", new NativeMethod() { public void invoke(NativeContext c) {
            device.midiShort(c.argInt(0), c.argInt(1), c.argInt(2)); } });
        r.register(AUDIO + "midiLong0([BII)V", new NativeMethod() { public void invoke(NativeContext c) {
            int off = c.argInt(1), len = c.argInt(2);
            byte[] b = new byte[len];
            Heap h = c.heap();
            for (int i = 0; i < len; i++) b[i] = h.getByte(c.argRef(0), off + i);
            device.midiLong(b, 0, len); } });
        r.register(AUDIO + "stage0([BII)V", new NativeMethod() { public void invoke(NativeContext c) {
            int len = c.argInt(2);
            if (len < 0) { stage = new ByteArrayOutputStream(); stageOverflow = false; return; }
            if (stage.size() + (long) len > MAX_CLIP_BYTES) { stageOverflow = true; return; }
            int off = c.argInt(1);
            byte[] b = new byte[len];
            Heap h = c.heap();
            for (int i = 0; i < len; i++) b[i] = h.getByte(c.argRef(0), off + i);
            stage.write(b, 0, len);
            gs.charge(len / 16 + 8); } });
        r.register(AUDIO + "source0(Ljava/lang/String;)I", new NativeMethod() { public void invoke(NativeContext c) {
            if (stageOverflow) { stage = new ByteArrayOutputStream(); stageOverflow = false; c.retInt(-1); return; }
            int id = nextSource++;
            sources.put(Integer.valueOf(id), new Object[] {stage.toByteArray(), gs.string(c.argRef(0))});
            stage = new ByteArrayOutputStream();
            c.retInt(id); } });
        r.register(AUDIO + "freeSource0(I)V", new NativeMethod() { public void invoke(NativeContext c) { sources.remove(Integer.valueOf(c.argInt(0))); } });
        r.register(AUDIO + "open0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            Object[] s = sources.get(Integer.valueOf(c.argInt(0)));
            if (s == null) throw new VmThrow("java/lang/IllegalStateException", "bad audio source");
            int h = device.open((byte[]) s[0], (String) s[1]);
            if (h <= 0) gs.log(2, "media", "audio cannot be played: " + s[1] + ", " + ((byte[]) s[0]).length + " bytes");
            if (h > 0) synchronized (AudioNatives.this) { clips.put(Integer.valueOf(h), new Wait()); }
            c.retInt(h > 0 ? h : -1); } });
        r.register(AUDIO + "duration0(I)J", new NativeMethod() { public void invoke(NativeContext c) { waitOf(c.argInt(0)); c.retLong(device.durationMs(c.argInt(0))); } });
        r.register(AUDIO + "play0(III)V", new NativeMethod() { public void invoke(NativeContext c) {
            int clip = c.argInt(0);
            Wait w = waitOf(clip);
            synchronized (AudioNatives.this) { release(w); w.run = c.argInt(2); }
            device.play(clip, c.argInt(1)); } });
        r.register(AUDIO + "stop0(I)V", new NativeMethod() { public void invoke(NativeContext c) {
            int clip = c.argInt(0);
            Wait w = waitOf(clip);
            device.stop(clip);
            synchronized (AudioNatives.this) { release(w); } } });
        r.register(AUDIO + "position0(I)J", new NativeMethod() { public void invoke(NativeContext c) { waitOf(c.argInt(0)); c.retLong(device.positionMs(c.argInt(0))); } });
        r.register(AUDIO + "seek0(IJ)V", new NativeMethod() { public void invoke(NativeContext c) { waitOf(c.argInt(0)); device.seek(c.argInt(0), c.argLong(1)); } });
        r.register(AUDIO + "volume0(II)V", new NativeMethod() { public void invoke(NativeContext c) { waitOf(c.argInt(0)); device.volume(c.argInt(0), c.argInt(1)); } });
        r.register(AUDIO + "tempo0(II)I", new NativeMethod() { public void invoke(NativeContext c) { waitOf(c.argInt(0)); c.retInt(device.tempo(c.argInt(0), c.argInt(1))); } });
        r.register(AUDIO + "rate0(II)I", new NativeMethod() { public void invoke(NativeContext c) { waitOf(c.argInt(0)); c.retInt(device.rate(c.argInt(0), c.argInt(1))); } });
        r.register(AUDIO + "close0(I)V", new NativeMethod() { public void invoke(NativeContext c) {
            int clip = c.argInt(0);
            Wait w;
            synchronized (AudioNatives.this) { w = clips.remove(Integer.valueOf(clip)); if (w != null) release(w); }
            if (w != null) device.close(clip); } });
        r.register(AUDIO + "await0(II)I", new NativeMethod() { public void invoke(NativeContext c) {
            Wait w = waitOf(c.argInt(0));
            synchronized (AudioNatives.this) {
                if (w.run != c.argInt(1)) { c.retInt(0); return; }
                if (w.ended) { w.ended = false; w.run = -1; c.retInt(1); return; }
                if (w.blocker != null) { c.retInt(0); return; }
                Pending p = new Pending();
                w.blocker = p;
                c.block(p);
            } } });
    }
}
