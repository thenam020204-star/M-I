/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package javax.microedition.shell;

import android.util.Log;

import androidx.annotation.Keep;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jlkvm.host.audio.SynthLibrary;
import ru.woesss.j2me.mmapi.synth.Library;
import ru.woesss.j2me.mmapi.synth.eas.LibEAS;
import ru.woesss.j2me.mmapi.synth.tsf.LibTSF;

/**
 * Adapts JL-Mod's native MIDI synthesisers ({@code mmapi_eas} = Sonivox, {@code mmapi_tsf} = TinySoundFont, loaded through the existing
 * JNI classes) to the VM's {@link SynthLibrary} (PLAN T35c). The libraries are created on first use, so a game without MIDI never loads
 * them; a library that cannot load (sound bank not supported, missing ABI) makes every call fail with an exception, which
 * {@code AudioRouter} turns into "cannot play" (-1). UNVERIFIED on a device (V16).
 */
final class VmSynth implements SynthLibrary {
    private static final String TAG = "VmSynth";

    private interface Factory {
        Library create() throws Throwable;
    }

    private static final Object LOCK = new Object();
    private static Library sharedEas;
    private static boolean easFailed;

    /** Sonivox with the game's sound bank (DLS) or its built-in one; cached: clips and raw MIDI share one instance. */
    private static Library eas(String bank) throws Throwable {
        synchronized (LOCK) {
            if (sharedEas != null) return sharedEas;
            if (easFailed) throw new IllegalStateException("Sonivox unavailable");
            try {
                sharedEas = bank != null ? new LibEAS(bank) : new LibEAS();
                return sharedEas;
            } catch (Throwable e) {
                Log.w(TAG, "Sonivox with sound bank failed: " + e);
                if (bank != null) {
                    try {
                        sharedEas = new LibEAS();
                        return sharedEas;
                    } catch (Throwable e2) {
                        Log.w(TAG, "Sonivox without sound bank failed: " + e2);
                    }
                }
                easFailed = true;
                throw e;
            }
        }
    }

    /** MIDI files: Sonivox (DLS bank or built-in), TinySoundFont when the bank is an SF2 that Sonivox rejects. */
    static VmSynth forClips(final String soundBank) {
        return new VmSynth(new Factory() {
            @Override public Library create() throws Throwable {
                if (soundBank == null) return eas(null);
                try {
                    return new LibTSF(soundBank);   // an SF2 / SF3 bank: TinySoundFont plays it, Sonivox cannot
                } catch (Throwable e) {
                    Log.w(TAG, "TinySoundFont failed: " + e);
                    return eas(soundBank);
                }
            }
        });
    }

    /** The raw MIDI port (MIDIControl messages): only Sonivox takes them. */
    static VmSynth forRaw(final String soundBank) {
        return new VmSynth(new Factory() {
            @Override public Library create() throws Throwable {
                return eas(soundBank);
            }
        });
    }

    /** The object the native player calls back ({@code postEvent(int, long)} by name, see PlayerListener.cpp). */
    private static final class Sink {
        private final long handle;
        private final VmSynth owner;

        Sink(long handle, VmSynth owner) {
            this.handle = handle;
            this.owner = owner;
        }

        /** @noinspection unused */
        @Keep // called from native
        private void postEvent(int type, long time) {
            EventListener l = owner.listeners.get(handle);
            if (l != null) l.event(handle, type, time);
        }
    }

    private final Factory factory;
    private volatile Library lib;
    private Throwable failure;
    private final Map<Long, EventListener> listeners = new ConcurrentHashMap<>();
    private final Map<Long, Sink> sinks = new ConcurrentHashMap<>();   // the native side holds a global ref; keep ours so R8 / GC see the class used

    private VmSynth(Factory factory) {
        this.factory = factory;
    }

    private synchronized Library lib() throws Exception {
        if (lib != null) return lib;
        if (failure != null) throw new IllegalStateException("synthesiser unavailable: " + failure);
        try {
            lib = factory.create();
            return lib;
        } catch (Throwable e) {
            failure = e;
            Log.w(TAG, "synthesiser unavailable", e);
            throw new IllegalStateException("synthesiser unavailable: " + e);
        }
    }

    @Override
    public long createPlayer(String locator) throws Exception {
        return lib().createPlayer(locator);
    }

    @Override
    public void setEventListener(long handle, EventListener l) {
        listeners.put(handle, l);
        Sink s = new Sink(handle, this);
        sinks.put(handle, s);
        try {
            lib.setListener(handle, s);
        } catch (RuntimeException e) {
            Log.w(TAG, "setListener failed", e);
        }
    }

    @Override public void setDataSource(long h, byte[] data) throws Exception { lib().setDataSource(h, data); }

    @Override public void realize(long h) throws Exception { lib().realize(h); }

    @Override public void prefetch(long h) throws Exception { lib().prefetch(h); }

    @Override public void start(long h) throws Exception { lib().start(h); }

    @Override public void pause(long h) throws Exception { lib().pause(h); }

    @Override public void deallocate(long h) { lib.deallocate(h); }

    @Override
    public void close(long h) {
        listeners.remove(h);
        lib.close(h);
    }

    @Override
    public void release(long h) {
        lib.finalize(h);
        sinks.remove(h);
    }

    @Override public long setMediaTime(long h, long us) { return lib.setMediaTime(h, us); }

    @Override public long getMediaTime(long h) { return lib.getMediaTime(h); }

    @Override public void setRepeat(long h, int count) { lib.setRepeat(h, count); }

    @Override public void setVolume(long h, float l, float r) { lib.setVolume(h, l, r); }

    @Override public long getDuration(long h) { return lib.getDuration(h); }

    @Override public int writeMIDI(long h, byte[] d, int off, int len) { return lib.writeMIDI(h, d, off, len); }
}
