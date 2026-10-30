package jlkvm.host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.heap.Heap;
import jlkvm.host.net.HttpTransport;
import jlkvm.natives.AsyncBlocker;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * Natives of {@code jlkvm.internal.Net} (PLAN T26b): the HTTP handler of the guest asks the host for one request at a time. The profile's
 * network policy for "http" is checked here again (defence in depth: Connector already refuses deny / offline), so a host never touches
 * the network unless the profile says "allow". The request runs on a worker thread; only the calling guest thread waits (R7, D5).
 */
public final class NetNatives {
    private static final String NET = "jlkvm/internal/Net.";
    private final DeviceProfile profile;
    private final HttpTransport transport;
    private final Map<Integer, HttpTransport.Response> responses = new HashMap<Integer, HttpTransport.Response>();
    private int next = 1;
    private String lastError;

    public NetNatives(DeviceProfile profile, HttpTransport transport) {
        this.profile = profile;
        this.transport = transport;
    }

    private HttpTransport.Response get(int h) {
        HttpTransport.Response r = responses.get(Integer.valueOf(h));
        if (r == null) throw new jlkvm.link.VmThrow("java/lang/IllegalStateException", "bad http handle " + h);
        return r;
    }

    public void register(NativeRegistry r) {
        final GuestServices gs = r.services();
        r.register(NET + "http0(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;[BII)I", new NativeMethod() { public void invoke(final NativeContext c) {
            if (profile.networkPolicy("http") != DeviceProfile.NET_ALLOW) { c.retInt(-2); return; }
            final String method = gs.string(c.argRef(0)), url = gs.string(c.argRef(1));
            Heap h = c.heap();
            int ha = c.argRef(2);
            final List<String[]> headers = new ArrayList<String[]>();
            for (int i = 0; i + 1 < h.arrayLength(ha); i += 2) headers.add(new String[] {gs.string(h.getRefElem(ha, i)), gs.string(h.getRefElem(ha, i + 1))});
            int len = c.argInt(4);
            final byte[] body = new byte[len];
            for (int i = 0; i < len; i++) body[i] = h.getByte(c.argRef(3), i);
            final int timeout = c.argInt(5);
            gs.charge(len / 16 + 64);
            final HttpTransport.Response[] result = new HttpTransport.Response[1];
            final String[] error = new String[1];
            final AsyncBlocker b = new AsyncBlocker() {
                protected void complete(NativeContext cc) {
                    if (result[0] == null) { lastError = error[0]; cc.retInt(-1); return; }
                    int id = next++;
                    responses.put(Integer.valueOf(id), result[0]);
                    cc.retInt(id);
                } };
            c.block(b);
            Thread t = new Thread("jlkvm-http") { public void run() {
                try {
                    result[0] = transport.execute(method, url, headers, body, timeout);
                } catch (IOException e) {
                    error[0] = e.toString();
                } catch (RuntimeException e) {
                    error[0] = e.toString();
                } finally {
                    b.finish();
                } } };
            t.setDaemon(true);
            t.start(); } });
        r.register(NET + "error0()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(lastError == null ? 0 : gs.newString(lastError)); } });
        r.register(NET + "status0(I)I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(get(c.argInt(0)).status); } });
        r.register(NET + "message0(I)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            String m = get(c.argInt(0)).message;
            c.retRef(m == null ? 0 : gs.newString(m)); } });
        r.register(NET + "headerCount0(I)I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(get(c.argInt(0)).headers.size()); } });
        r.register(NET + "headerKey0(II)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(gs.newString(get(c.argInt(0)).headers.get(c.argInt(1))[0])); } });
        r.register(NET + "headerValue0(II)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(gs.newString(get(c.argInt(0)).headers.get(c.argInt(1))[1])); } });
        r.register(NET + "length0(I)I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(get(c.argInt(0)).body.length); } });
        r.register(NET + "read0(II[BII)I", new NativeMethod() { public void invoke(NativeContext c) {
            byte[] body = get(c.argInt(0)).body;
            int pos = c.argInt(1), arr = c.argRef(2), off = c.argInt(3), n = Math.min(c.argInt(4), body.length - pos);
            if (n <= 0) { c.retInt(0); return; }
            Heap h = c.heap();
            for (int i = 0; i < n; i++) h.putByte(arr, off + i, body[pos + i]);
            gs.charge(n / 16 + 8);
            c.retInt(n); } });
        r.register(NET + "free0(I)V", new NativeMethod() { public void invoke(NativeContext c) { responses.remove(Integer.valueOf(c.argInt(0))); } });
    }
}
