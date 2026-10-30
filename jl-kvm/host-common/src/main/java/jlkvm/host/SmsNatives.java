package jlkvm.host;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.natives.AsyncBlocker;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * Natives of {@code jlkvm.internal.wma.Sms} (PLAN T32a): the SMS / CBS adapter of WMA 1.1 over NOTHING real. The profile's {@code sms.mode}
 * decides here again (defence in depth, the guest {@code Connector} has already applied it): only "simulate" lets a send succeed, and even then
 * nothing leaves the host - the message is appended to {@link #sent()} and handed to the {@link Sink}. "deny" answers -1 (SecurityException in the
 * guest), "offline" answers -2 (IOException). Incoming messages exist only when a test host pushes them with {@link #deliverText} /
 * {@link #deliverBinary} (any thread); they are queued per reserved port. A guest thread parked in {@code await0} / {@code awaitNew0} is completed
 * through an {@link AsyncBlocker}, so listeners always run on guest threads (D13). docs/jsr.md.
 */
public final class SmsNatives {
    private static final String SMS = "jlkvm/internal/wma/Sms.";

    /** Receives every message the guest sent in "simulate" mode (on the vm-thread). */
    public interface Sink {
        void sent(String line);
    }

    private static final class Msg {
        boolean binary;
        String from, text;
        byte[] data;
        long time;
    }

    private static final class Pending extends AsyncBlocker {
        volatile int result;
        protected void complete(NativeContext c) { c.retInt(result); }
    }

    private static final class Port {
        final String key;
        final LinkedList<Msg> queue = new LinkedList<Msg>();
        long arrived, consumed, notified;
        boolean listening, closed;
        Pending recvWaiter, listenWaiter;
        Port(String key) { this.key = key; }
    }

    private final DeviceProfile profile;
    private final Map<Integer, Port> ports = new HashMap<Integer, Port>();
    private final Map<String, Port> byKey = new HashMap<String, Port>();
    private final List<String> sent = Collections.synchronizedList(new ArrayList<String>());
    private Sink sink;
    private int next = 1;
    private Msg current;

    public SmsNatives(DeviceProfile profile) { this.profile = profile; }

    /** Where simulated sends are reported (default: only {@link #sent()}). */
    public SmsNatives sink(Sink s) { this.sink = s; return this; }

    /** One line per message the guest "sent" in simulate mode: {@code to=ADDRESS;text=...;segments=N} or {@code to=ADDRESS;data=HEX;segments=N}. */
    public List<String> sent() { return sent; }

    public boolean sendAllowed() { return profile.smsMode.equals("simulate"); }

    /** The port of a handle, null once it was released (a guest thread may still be on its way into a native). */
    private synchronized Port port(int h) { return ports.get(Integer.valueOf(h)); }

    private static boolean deliver0(Port p, Msg m) {
        p.queue.addLast(m);
        p.arrived++;
        if (p.recvWaiter != null) { p.recvWaiter.result = 1; p.recvWaiter.finish(); p.recvWaiter = null; }
        if (p.listenWaiter != null) {
            p.notified = Math.max(p.notified, p.consumed) + 1;
            p.listenWaiter.result = 1; p.listenWaiter.finish(); p.listenWaiter = null;
        }
        return true;
    }

    private synchronized boolean deliver(boolean cbs, int port, Msg m) {
        if (!sendAllowed()) return false;   // deny / offline: nothing comes in either
        Port p = byKey.get((cbs ? "cbs:" : "sms:") + port);
        if (p == null) return false;
        return deliver0(p, m);
    }

    /**
     * Test host: an SMS (or CBS) text message arrives on the reserved {@code port}. @return false when the mode is not "simulate" or no connection
     * holds the port (the message is dropped). Any thread.
     */
    public boolean deliverText(boolean cbs, int port, String fromAddress, String text, long timeMillis) {
        Msg m = new Msg(); m.from = fromAddress; m.text = text; m.time = timeMillis;
        return deliver(cbs, port, m);
    }

    public boolean deliverBinary(boolean cbs, int port, String fromAddress, byte[] data, long timeMillis) {
        Msg m = new Msg(); m.binary = true; m.from = fromAddress; m.data = data.clone(); m.time = timeMillis;
        return deliver(cbs, port, m);
    }

    private static String hex(byte[] d) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < d.length; i++) { sb.append(Character.forDigit((d[i] >> 4) & 15, 16)).append(Character.forDigit(d[i] & 15, 16)); }
        return sb.toString();
    }

    private int record(String line) {
        if (!sendAllowed()) return profile.smsMode.equals("offline") ? -2 : -1;
        sent.add(line);
        if (sink != null) sink.sent(line);
        return 0;
    }

    public void register(NativeRegistry r) {
        final GuestServices gs = r.services();
        r.register(SMS + "bind0(II)I", new NativeMethod() { public void invoke(NativeContext c) {
            if (!sendAllowed()) { c.retInt(profile.smsMode.equals("offline") ? -2 : -1); return; }
            String key = (c.argInt(0) != 0 ? "cbs:" : "sms:") + c.argInt(1);
            synchronized (SmsNatives.this) {
                if (byKey.containsKey(key)) { c.retInt(-3); return; }
                Port p = new Port(key);
                int h = next++;
                ports.put(Integer.valueOf(h), p);
                byKey.put(key, p);
                c.retInt(h);
            } } });
        r.register(SMS + "unbind0(I)V", new NativeMethod() { public void invoke(NativeContext c) {
            synchronized (SmsNatives.this) {
                Port p = ports.remove(Integer.valueOf(c.argInt(0)));
                if (p == null) return;
                byKey.remove(p.key);
                p.closed = true;
                if (p.recvWaiter != null) { p.recvWaiter.result = 0; p.recvWaiter.finish(); p.recvWaiter = null; }
                if (p.listenWaiter != null) { p.listenWaiter.result = 0; p.listenWaiter.finish(); p.listenWaiter = null; }
            } } });
        r.register(SMS + "sendText0(Ljava/lang/String;Ljava/lang/String;I)I", new NativeMethod() { public void invoke(NativeContext c) {
            String to = gs.string(c.argRef(0)), text = gs.string(c.argRef(1));
            c.retInt(record("to=" + to + ";text=" + text + ";segments=" + c.argInt(2))); } });
        r.register(SMS + "sendBinary0(Ljava/lang/String;[BI)I", new NativeMethod() { public void invoke(NativeContext c) {
            String to = gs.string(c.argRef(0));
            int a = c.argRef(1), n = c.heap().arrayLength(a);
            byte[] d = new byte[n];
            for (int i = 0; i < n; i++) d[i] = c.heap().getByte(a, i);
            c.retInt(record("to=" + to + ";data=" + hex(d) + ";segments=" + c.argInt(2))); } });
        r.register(SMS + "await0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            synchronized (SmsNatives.this) {
                Port p = port(c.argInt(0));
                if (p == null || p.closed) { c.retInt(0); return; }
                if (!p.queue.isEmpty()) { c.retInt(1); return; }
                if (p.recvWaiter != null) { p.recvWaiter.result = 0; p.recvWaiter.finish(); }
                Pending w = new Pending();
                p.recvWaiter = w;
                c.block(w);
            } } });
        r.register(SMS + "take0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            synchronized (SmsNatives.this) {
                Port p = port(c.argInt(0));
                current = p == null ? null : p.queue.pollFirst();
                if (current == null) { c.retInt(0); return; }
                p.consumed++;
                c.retInt(current.binary ? 2 : 1);
            } } });
        r.register(SMS + "curAddress0()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(current == null || current.from == null ? 0 : gs.newString(current.from)); } });
        r.register(SMS + "curText0()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(current == null || current.text == null ? 0 : gs.newString(current.text)); } });
        r.register(SMS + "curData0()[B", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(current == null || current.data == null ? 0 : gs.newByteArray(current.data, 0, current.data.length)); } });
        r.register(SMS + "curTime0()J", new NativeMethod() { public void invoke(NativeContext c) { c.retLong(current == null ? 0 : current.time); } });
        r.register(SMS + "listening0(IZ)V", new NativeMethod() { public void invoke(NativeContext c) {
            synchronized (SmsNatives.this) {
                Port p = port(c.argInt(0));
                if (p == null) return;
                boolean on = c.argInt(1) != 0;
                p.listening = on;
                if (on) p.notified = p.consumed;
                else if (p.listenWaiter != null) { p.listenWaiter.result = 0; p.listenWaiter.finish(); p.listenWaiter = null; }
            } } });
        r.register(SMS + "awaitNew0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            synchronized (SmsNatives.this) {
                Port p = port(c.argInt(0));
                if (p == null || p.closed || !p.listening) { c.retInt(0); return; }
                long target = Math.max(p.notified, p.consumed);
                if (p.arrived > target) { p.notified = target + 1; c.retInt(1); return; }
                if (p.listenWaiter != null) { p.listenWaiter.result = 0; p.listenWaiter.finish(); }
                Pending w = new Pending();
                p.listenWaiter = w;
                c.block(w);
            } } });
    }
}
