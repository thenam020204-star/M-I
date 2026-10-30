package jlkvm.host;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.host.fs.HostFs;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * Natives of {@code jlkvm.internal.io.file.Fs} (PLAN T32a): JSR-75 FileConnection over a {@link HostFs}. The profile permission
 * {@code file.permission} is enforced HERE on every call (deny: any call is a SecurityException; read: calls that change anything are), so
 * the guest layer is not trusted. A host error (IOException of the HostFs, which also reports a path that leaves the sandbox) becomes a guest
 * IOException. docs/jsr.md.
 */
public final class FileNatives {
    private static final String FS = "jlkvm/internal/io/file/Fs.";
    /** Most host files open at the same time (a leaked stream cannot exhaust the host). */
    public static final int MAX_OPEN = 64;

    private final DeviceProfile profile;
    private final HostFs fs;
    private final Map<Integer, HostFs.Handle> open = new HashMap<Integer, HostFs.Handle>();
    private int next = 1;

    public FileNatives(DeviceProfile profile, HostFs fs) {
        this.profile = profile;
        this.fs = fs;
    }

    private int perm() { return profile.filePermission.equals("deny") ? 0 : profile.filePermission.equals("read") ? 1 : 2; }

    private boolean denied(NativeContext c, boolean write) {
        int p = perm();
        if (p == 0 || (write && p < 2)) {
            c.throwNew("java/lang/SecurityException", write && p == 1 ? "write access to files denied by the device profile" : "file system access denied by the device profile");
            return true;
        }
        return false;
    }

    private void io(NativeContext c, IOException e) { c.throwNew("java/io/IOException", e.getMessage()); }

    private HostFs.Handle handle(NativeContext c, int h) {
        HostFs.Handle x = open.get(Integer.valueOf(h));
        if (x == null) c.throwNew("java/io/IOException", "stream closed");
        return x;
    }

    public void register(NativeRegistry r) {
        final GuestServices gs = r.services();
        r.register(FS + "perm0()I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(perm()); } });
        r.register(FS + "roots0()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            StringBuilder sb = new StringBuilder();
            for (String s : fs.roots()) { if (sb.length() > 0) sb.append('\0'); sb.append(s); }
            c.retRef(gs.newString(sb.toString())); } });
        r.register(FS + "exists0(Ljava/lang/String;)Z", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            c.retInt(fs.exists(gs.string(c.argRef(0))) ? 1 : 0); } });
        r.register(FS + "isDir0(Ljava/lang/String;)Z", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            c.retInt(fs.isDirectory(gs.string(c.argRef(0))) ? 1 : 0); } });
        r.register(FS + "length0(Ljava/lang/String;)J", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            c.retLong(fs.length(gs.string(c.argRef(0)))); } });
        r.register(FS + "mtime0(Ljava/lang/String;)J", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            c.retLong(fs.lastModified(gs.string(c.argRef(0)))); } });
        r.register(FS + "list0(Ljava/lang/String;)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            try {
                StringBuilder sb = new StringBuilder();
                for (String s : fs.list(gs.string(c.argRef(0)))) { if (sb.length() > 0) sb.append('\0'); sb.append(s); }
                gs.charge(sb.length() / 16 + 16);
                c.retRef(gs.newString(sb.toString()));
            } catch (IOException e) { io(c, e); } } });
        r.register(FS + "create0(Ljava/lang/String;)V", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, true)) return;
            try { fs.create(gs.string(c.argRef(0))); } catch (IOException e) { io(c, e); } } });
        r.register(FS + "mkdir0(Ljava/lang/String;)V", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, true)) return;
            try { fs.mkdir(gs.string(c.argRef(0))); } catch (IOException e) { io(c, e); } } });
        r.register(FS + "delete0(Ljava/lang/String;)V", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, true)) return;
            try { fs.delete(gs.string(c.argRef(0))); } catch (IOException e) { io(c, e); } } });
        r.register(FS + "rename0(Ljava/lang/String;Ljava/lang/String;)V", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, true)) return;
            try { fs.rename(gs.string(c.argRef(0)), gs.string(c.argRef(1))); } catch (IOException e) { io(c, e); } } });
        r.register(FS + "truncate0(Ljava/lang/String;J)V", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, true)) return;
            try { fs.truncate(gs.string(c.argRef(0)), c.argLong(1)); } catch (IOException e) { io(c, e); } } });
        r.register(FS + "total0(Ljava/lang/String;)J", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            c.retLong(fs.total(gs.string(c.argRef(0)))); } });
        r.register(FS + "used0(Ljava/lang/String;)J", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            c.retLong(fs.used(gs.string(c.argRef(0)))); } });
        r.register(FS + "attr0(Ljava/lang/String;)I", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            c.retInt(fs.attrs(gs.string(c.argRef(0)))); } });
        r.register(FS + "setAttr0(Ljava/lang/String;IZ)V", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, true)) return;
            fs.setAttr(gs.string(c.argRef(0)), c.argInt(1), c.argInt(2) != 0); } });
        r.register(FS + "open0(Ljava/lang/String;Z)I", new NativeMethod() { public void invoke(NativeContext c) {
            boolean write = c.argInt(1) != 0;
            if (denied(c, write)) return;
            if (open.size() >= MAX_OPEN) { c.throwNew("java/io/IOException", "too many open files"); return; }
            try {
                HostFs.Handle h = fs.open(gs.string(c.argRef(0)), write);
                int id = next++;
                open.put(Integer.valueOf(id), h);
                c.retInt(id);
            } catch (IOException e) { io(c, e); } } });
        r.register(FS + "read0(IJ[BII)I", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, false)) return;
            HostFs.Handle h = handle(c, c.argInt(0));
            if (h == null) return;
            int arr = c.argRef(2), off = c.argInt(3), len = c.argInt(4);
            byte[] tmp = new byte[len];
            try {
                int n = h.read(c.argLong(1), tmp, 0, len);
                for (int i = 0; i < n; i++) c.heap().putByte(arr, off + i, tmp[i]);
                gs.charge(Math.max(n, 0) / 16 + 8);
                c.retInt(n);
            } catch (IOException e) { io(c, e); } } });
        r.register(FS + "write0(IJ[BII)V", new NativeMethod() { public void invoke(NativeContext c) {
            if (denied(c, true)) return;
            HostFs.Handle h = handle(c, c.argInt(0));
            if (h == null) return;
            int arr = c.argRef(2), off = c.argInt(3), len = c.argInt(4);
            byte[] tmp = new byte[len];
            for (int i = 0; i < len; i++) tmp[i] = c.heap().getByte(arr, off + i);
            try { h.write(c.argLong(1), tmp, 0, len); gs.charge(len / 16 + 8); } catch (IOException e) { io(c, e); } } });
        r.register(FS + "close0(I)V", new NativeMethod() { public void invoke(NativeContext c) {
            HostFs.Handle h = open.remove(Integer.valueOf(c.argInt(0)));
            if (h != null) h.close(); } });
    }
}
