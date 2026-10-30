package jlkvm.host;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jlkvm.DeviceProfile;
import jlkvm.host.fs.MemoryFs;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeRegistry;

/**
 * T32a defence in depth: the host natives check the profile policy themselves, whatever the guest layer did (a guest that skipped
 * {@code Connector} still gets nothing out of an sms.mode deny / offline host, nor a file in a file.permission deny / read one).
 */
public class PolicyNativesTest {
    /** A fake native call: ints/strings in, the first result and a thrown guest exception out. */
    private static final class Call implements InvocationHandler {
        final Object[] args;
        Object ret;
        String thrown;
        Call(Object... args) { this.args = args; }
        public Object invoke(Object proxy, Method m, Object[] a) {
            String n = m.getName();
            if (n.equals("argInt") || n.equals("argRef")) { Object v = args[(Integer) a[0]]; return v instanceof Integer ? v : Integer.valueOf(0); }
            if (n.equals("argLong")) return args[(Integer) a[0]];
            if (n.startsWith("ret")) { ret = a.length > 0 ? a[0] : null; return null; }
            if (n.equals("throwNew")) { thrown = a[0] + ": " + a[1]; return null; }
            if (n.equals("block")) { thrown = "BLOCKED"; return null; }
            throw new UnsupportedOperationException(n);
        }
    }

    private static NativeContext ctx(Call c) { return (NativeContext) Proxy.newProxyInstance(NativeContext.class.getClassLoader(), new Class<?>[] {NativeContext.class}, c); }

    /** Strings of the guest are kept in a list: reference i+1 is element i. */
    private static NativeRegistry registry(final List<String> strings) {
        NativeRegistry r = new NativeRegistry();
        r.setServices((GuestServices) Proxy.newProxyInstance(GuestServices.class.getClassLoader(), new Class<?>[] {GuestServices.class}, new InvocationHandler() {
            public Object invoke(Object p, Method m, Object[] a) {
                if (m.getName().equals("string")) return strings.get((Integer) a[0] - 1);
                if (m.getName().equals("newString")) { strings.add((String) a[0]); return strings.size(); }
                return null;
            } }));
        return r;
    }

    @org.junit.Test public void smsNativesRefuseUnlessSimulate() {
        for (String mode : new String[] {"deny", "offline", "simulate"}) {
            DeviceProfile p = new DeviceProfile();
            p.smsMode = mode;
            SmsNatives sms = new SmsNatives(p);
            List<String> strs = new ArrayList<String>(Arrays.asList("sms://+1", "hello"));
            NativeRegistry r = registry(strs);
            sms.register(r);
            Call send = new Call(1, 2, 1);
            r.lookup("jlkvm/internal/wma/Sms.sendText0(Ljava/lang/String;Ljava/lang/String;I)I").invoke(ctx(send));
            Call bind = new Call(0, 6000);
            r.lookup("jlkvm/internal/wma/Sms.bind0(II)I").invoke(ctx(bind));
            if (mode.equals("simulate")) {
                assertEquals(0, send.ret);
                assertTrue(((Integer) bind.ret) > 0);
                assertEquals(Arrays.asList("to=sms://+1;text=hello;segments=1"), sms.sent());
            } else {
                assertEquals(mode, mode.equals("deny") ? -1 : -2, send.ret);
                assertEquals(mode, mode.equals("deny") ? -1 : -2, bind.ret);
                assertTrue(mode, sms.sent().isEmpty());
                assertFalse(sms.deliverText(false, 6000, "x", "y", 0));
            }
        }
    }

    @org.junit.Test public void fileNativesCheckThePermission() {
        String[] perms = {"deny", "read", "readwrite"};
        for (String perm : perms) {
            DeviceProfile p = new DeviceProfile();
            p.filePermission = perm;
            MemoryFs fs = new MemoryFs(p.fileRoots, p.fileQuotaBytes);
            NativeRegistry r = registry(new ArrayList<String>(Arrays.asList("root1/x.txt")));
            new FileNatives(p, fs).register(r);
            Call create = new Call(1);
            r.lookup("jlkvm/internal/io/file/Fs.create0(Ljava/lang/String;)V").invoke(ctx(create));
            Call exists = new Call(1);
            r.lookup("jlkvm/internal/io/file/Fs.exists0(Ljava/lang/String;)Z").invoke(ctx(exists));
            if (perm.equals("readwrite")) {
                assertNull(create.thrown);
                assertTrue(fs.exists("root1/x.txt"));
                assertEquals(Integer.valueOf(1), exists.ret);
            } else {
                assertTrue(perm, create.thrown != null && create.thrown.startsWith("java/lang/SecurityException"));
                assertFalse(fs.exists("root1/x.txt"));
                if (perm.equals("deny")) assertTrue(exists.thrown != null && exists.thrown.startsWith("java/lang/SecurityException"));
                else assertNull(exists.thrown);
            }
        }
    }
}
