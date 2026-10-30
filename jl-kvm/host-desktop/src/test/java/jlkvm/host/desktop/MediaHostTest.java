package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import jlkvm.host.audio.RecordingAudioDevice;
import jlkvm.profile.ProfileLoader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * T27 on the desktop host: MMAPI over the headless audio device. RTTTL through the tone generator (no decoder), clips played by the
 * device whose end comes from the host (script verb "audio end N") and is delivered to a listener on a guest thread, the profile's content
 * type list, a device that refuses a type, and the JSR-135 controls (MIDIControl / TempoControl) that exist only when the profile
 * declares the "jsr-135" layer (D10).
 */
public class MediaHostTest {
    @Rule public TemporaryFolder tmp = new TemporaryFolder();

    private File profile(String apis, String media) throws Exception {
        String base = new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "profiles/jlkvm-min.json").toPath()), "UTF-8");
        String json = base.replaceAll("(?s)\"apis\"\\s*:\\s*\\[.*?\\]", "\"apis\": " + apis);
        assertNotEquals(base, json);
        if (media != null) json = json.replaceFirst("\\{", "{\"media\": " + media + ",");
        File f = tmp.newFile();
        Files.write(f.toPath(), json.getBytes("UTF-8"));
        return f;
    }

    private static String strip(List<String> log) {
        StringBuilder sb = new StringBuilder();
        for (String l : log) sb.append(l.replaceFirst("^t=\\d+ ", "")).append('\n');
        return sb.toString();
    }

    private static long time(String l) { return Long.parseLong(l.substring(2, l.indexOf(' '))); }

    private HeadlessRunner.Result run(String midlet, File profile, RecordingAudioDevice dev, String script) throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet(midlet);
        o.profile = profile;
        o.audio = dev;
        if (script != null) o.scriptText = script;
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertEquals(r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
        assertEquals(r.host.guestErr.toString(), 0, r.host.guestErr.length());
        assertEquals(0, r.vm.threadViolations);
        return r;
    }

    private static String script() throws Exception {
        return new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "tests/midlets/scripts/mmapi.inp").toPath()), "UTF-8");
    }

    /** Device that plays MIDI, WAV and AMR but not MP3. */
    private static RecordingAudioDevice device() {
        RecordingAudioDevice d = new RecordingAudioDevice();
        d.accepts = new HashSet<String>(Arrays.asList("audio/midi", "audio/x-wav", "audio/amr"));
        return d;
    }

    @Test public void clipsToneAndListenersOnGuestThreads() throws Exception {
        RecordingAudioDevice dev = device();
        HeadlessRunner.Result r = run("mmapi", null, dev, script());
        String guest = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertFalse(guest, guest.contains("UNEXPECTED"));
        assertTrue(guest, guest.contains("> types: [audio/x-tone-seq,audio/midi,audio/sp-midi,audio/x-wav,audio/mpeg,audio/amr,audio/x-rtttl,text/x-rtttl]\n"));
        assertTrue(guest, guest.contains("> amr: ok audio/amr\n> mp3 refused by the device: MediaException at realize\n"));
        assertTrue(guest, guest.contains("> rtttl waits for its end: true state PREFETCHED duration 750000\n> rtttl events started endOfMedia\n"));
        assertTrue(guest, guest.contains("> midi: UNREALIZED\n> midi realized: REALIZED type audio/midi duration -1\n"));
        assertTrue(guest, guest.contains("> midi has MIDIControl: false\n> midi has TempoControl: false RateControl: false\n"));
        assertTrue(guest, guest.contains("> midi started: STARTED\n> waiting for the host: true\n> midi ended: PREFETCHED events started volumeChanged endOfMedia\n"));
        assertTrue(guest, guest.contains("> midi restarted: STARTED\n> midi stopped: PREFETCHED events started stopped\n> midi deallocated: REALIZED\n> midi closed: CLOSED events closed\n"));
        assertTrue(guest, guest.contains("> wav type audio/x-wav duration 500000\n"));
        assertTrue(guest, guest.contains("> wav ended: PREFETCHED media time = duration true events started endOfMedia\n"));
        assertTrue(guest, guest.endsWith("> listener only on guest threads other than main: true\n"));
        // RTTTL = two tones 500 ms apart (the pause between), then playTone; no clip is ever opened for it
        List<String> log = dev.log();
        List<String> tones = new ArrayList<String>();
        for (String l : log) if (l.contains(" tone ")) tones.add(l);
        assertEquals(log.toString(), 3, tones.size());
        assertTrue(tones.get(0), tones.get(0).endsWith(" tone 72 250 100"));
        assertTrue(tones.get(1), tones.get(1).endsWith(" tone 76 250 100"));
        assertEquals(500, time(tones.get(1)) - time(tones.get(0)));
        assertTrue(tones.get(2), tones.get(2).endsWith(" tone 69 100 80"));
        assertEquals(6800, dev.pcmSamples());
        // clips: AMR probe = 1, MIDI = 2 (2 loops, volume 40, restarted, stopped, closed), WAV = 3
        String s = strip(log);
        assertTrue(s, s.contains("open 1 audio/amr 6 bytes duration=-1\nclose 1\n"));
        assertTrue(s, s.contains("open 2 audio/midi 14 bytes duration=-1\n"));
        assertTrue(s, s.contains("volume 2 100\nseek 2 0\nplay 2 loops=2 at=0\nvolume 2 40\n"));
        assertTrue(s, s.contains("ended 2\n"));
        assertTrue(s, s.contains("open 3 audio/x-wav 4044 bytes duration=500\n"));
        assertTrue(s, s.indexOf("play 3 loops=1") > s.indexOf("open 3") && s.contains("ended 3\nclose 3\n"));
        assertEquals(java.util.Collections.emptyList(), dev.openClips());
        // the end of clip 2 was announced by the host at script time 2000, not before
        for (String l : log) if (l.endsWith(" ended 2")) assertEquals(2000, time(l));
    }

    @Test public void profileDecidesTypesAndProtocols() throws Exception {
        File p = profile("[\"cldc-1.1\", \"midp-2.0\"]", "{\"contentTypes\": [\"audio/x-tone-seq\", \"audio/midi\", \"audio/x-rtttl\"], \"protocols\": [\"device\", \"resource\"]}");
        HeadlessRunner.Result r = run("mmapi", p, device(), script());
        String guest = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertTrue(guest, guest.contains("> types: [audio/x-tone-seq,audio/midi,audio/x-rtttl]\n> protocols: [device,resource]\n> midi protocols: [resource]\n"));
        assertTrue(guest, guest.contains("> amr: MediaException at create\n> mp3 refused by the device: MediaException at create\n"));
        assertTrue(guest, guest.contains("> wav: MediaException (Unsupported content type: audio/x-wav)\n"));
        // the profile loader knows the media keys and rejects others
        assertFalse(ProfileLoader.fromJson("{\"media\": {\"tone\": false}}").mediaTone);
        try { ProfileLoader.fromJson("{\"media\": {\"colour\": 1}}"); fail(); } catch (IllegalArgumentException expected) { }
    }

    @Test public void jsr135ControlsExistOnlyWhenTheProfileDeclaresThem() throws Exception {
        File full = profile("[\"cldc-1.1\", \"midp-2.0\", \"jsr-135\"]", null);
        File plain = profile("[\"cldc-1.1\", \"midp-2.0\"]", null);
        File romDir = HeadlessRunner.findRomDir();
        jlkvm.DeviceProfile dp = ProfileLoader.fromJson(new String(Files.readAllBytes(plain.toPath()), "UTF-8"));
        jlkvm.DeviceProfile df = ProfileLoader.fromJson(new String(Files.readAllBytes(full.toPath()), "UTF-8"));
        assertNull(new RomLayers(romDir, dp).readClass("javax/microedition/media/control/MIDIControl"));
        assertNull(new RomLayers(romDir, dp).readClass("javax/microedition/media/control/TempoControl"));
        assertNotNull(new RomLayers(romDir, df).readClass("javax/microedition/media/control/MIDIControl"));
        assertNotNull(new RomLayers(romDir, df).readClass("javax/microedition/media/control/TempoControl"));
        assertNotNull(new RomLayers(romDir, df).readClass("javax/microedition/media/control/RateControl"));
        // the MIDlet that does not name JSR-135 classes sees the controls through getControl only with the layer
        RecordingAudioDevice dev = device();
        String guest = DesktopTestSupport.onlyGuestLines(run("mmapi", full, dev, script()).stdout);
        assertTrue(guest, guest.contains("> midi has MIDIControl: true\n> midi has TempoControl: true RateControl: true\n"));
        // MmApi135 uses the interfaces
        dev = device();
        HeadlessRunner.Result r = run("mmapi135", full, dev, null);
        String g = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertFalse(g, g.contains("UNEXPECTED"));
        assertEquals(read("tests/midlets/expected/mmapi135.txt"), g);
        String s = strip(dev.log());
        assertTrue(s, s.contains("midi 145 60 100\nmidi 194 7 0\nmidi 178 7 90\nmidi 179 7 33\n"));
        assertTrue(s, s.contains("midi 180 0 1\nmidi 180 32 1\nmidi 196 5 0\n"));
        assertTrue(s, s.contains("midiLong 240 126 127 9 1 247\nmidiLong 2 3\n"));
        assertTrue(s, s.contains("tempo 1 90000\n"));
    }

    private static String read(String rel) throws Exception {
        return new String(Files.readAllBytes(new File(DesktopTestSupport.root(), rel).toPath()), "UTF-8");
    }
}
