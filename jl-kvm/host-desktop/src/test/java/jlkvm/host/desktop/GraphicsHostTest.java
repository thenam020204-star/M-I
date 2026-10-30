package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import org.junit.Test;

/** T23 on the desktop host: pixel fingerprints of the graphics MIDlet, determinism, image memory in the guest heap, host handle release. */
public class GraphicsHostTest {
    private static String expected(String id) throws Exception {
        return new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "tests/vmspec/cases/" + id + "/expected.txt").toPath()), "UTF-8");
    }

    private static HeadlessRunner.Result run(File jar, File profile) throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = jar;
        o.profile = profile;
        return HeadlessRunner.run(o);
    }

    @Test public void graphicsMidletFingerprintsAreStable() throws Exception {
        File jar = DesktopTestSupport.vmspec("gfx01_graphics");
        String a = DesktopTestSupport.onlyGuestLines(run(jar, null).stdout);
        assertEquals(expected("gfx01_graphics"), a);
        assertEquals(a, DesktopTestSupport.onlyGuestLines(run(jar, null).stdout));   // twice: identical
    }

    private static long freeDelta(String out) {
        for (String l : DesktopTestSupport.lines(out)) {
            if (l.startsWith("# freeMemory delta")) {
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("after 10 images 200x200: (-?\\d+) bytes").matcher(l);
                if (m.find()) return Long.parseLong(m.group(1));
            }
        }
        throw new AssertionError(out);
    }

    @Test public void imagePixelMemoryCountsInTheHeapOnlyWhenTheProfileSaysSo() throws Exception {
        File jar = DesktopTestSupport.vmspec("img01_image_memory");
        File off = File.createTempFile("prof", ".json"), on = File.createTempFile("prof", ".json");
        Files.write(off.toPath(), "{\"heapBytes\":4194304,\"imagesCountInHeap\":false}".getBytes("UTF-8"));
        Files.write(on.toPath(), "{\"heapBytes\":4194304,\"imagesCountInHeap\":true}".getBytes("UTF-8"));
        HeadlessRunner.Result a = run(jar, off), b = run(jar, on);
        assertTrue(a.stdout, freeDelta(a.stdout) < 20000);
        assertTrue(b.stdout, freeDelta(b.stdout) >= 10L * 200 * 200 * 4);
        // the guest released every image: the host raster handles of dead Images are gone (D8)
        assertEquals(0, a.vm.handles.liveCount());
        assertEquals(0, b.vm.handles.liveCount());
        // and with a heap too small for the pixels the OutOfMemoryError is the guest's, not a host crash
        File small = File.createTempFile("prof", ".json");
        Files.write(small.toPath(), "{\"heapBytes\":1048576,\"imagesCountInHeap\":true}".getBytes("UTF-8"));
        HeadlessRunner.Result c = run(jar, small);
        assertTrue(c.host.guestErr.toString(), c.host.guestErr.toString().contains("OutOfMemoryError"));
    }

    @Test public void presentedFramesAreHashable() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("events");
        o.scriptText = "100 shot first\n200 key tap 53\n300 shot second\n400 shot third\n500 destroy 1\n";
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertEquals(3, r.shots.size());
        assertEquals(64, r.shots.get("first").length());
        assertEquals(r.shots.get("first"), r.shots.get("second"));   // the repaint painted the same picture
        assertEquals(240 * 294, r.services.lastFrame.length);
        assertEquals(0xFF102030, r.services.lastFrame[0]);
    }
}
