package vmspec;

import java.util.Enumeration;
import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.UUID;
import javax.microedition.midlet.MIDlet;

/** T32 (JSR-82): the value classes UUID, DataElement, DeviceClass and the constants, which need no hardware. */
public class Bt02Values extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static String hex(Object o) {
        byte[] b = (byte[]) o;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) sb.append(Integer.toHexString((b[i] & 0xFF) | 0x100).substring(1));
        return sb.toString();
    }

    protected void startApp() {
        UUID u = new UUID(0x1101L);
        p("uuid16: " + u);
        p("uuid32: " + new UUID(0xABCDEF12L));
        p("uuid string short: " + new UUID("1101", true) + " equals " + u.equals(new UUID("1101", true)));
        p("uuid string 128: " + new UUID("0000110100001000800000805f9b34fb", false) + " equals " + u.equals(new UUID("0000110100001000800000805f9b34fb", false)));
        p("uuid short pad: " + new UUID("1", false));
        p("uuid equals null/other: " + u.equals(null) + " " + u.equals("x") + " hash " + (u.hashCode() == new UUID(0x1101L).hashCode()));
        p("uuid other: " + u.equals(new UUID(0x1102L)));
        try { new UUID(-1L); p("neg: ok"); } catch (Throwable t) { p("neg: " + cn(t)); }
        try { new UUID(0x100000000L); p("2^32: ok"); } catch (Throwable t) { p("2^32: " + cn(t)); }
        try { new UUID("", true); p("empty: ok"); } catch (Throwable t) { p("empty: " + cn(t)); }
        try { new UUID("123456789", true); p("9 chars short: ok"); } catch (Throwable t) { p("9 chars short: " + cn(t)); }
        try { new UUID("0123456789012345678901234567890123", false); p("34 chars: ok"); } catch (Throwable t) { p("34 chars: " + cn(t)); }
        try { new UUID("12G4", true); p("non-hex: ok"); } catch (Throwable t) { p("non-hex: " + cn(t)); }
        try { new UUID("0x11", true); p("0x prefix: ok"); } catch (Throwable t) { p("0x prefix: " + cn(t)); }
        try { new UUID(null, true); p("null: ok"); } catch (Throwable t) { p("null: " + cn(t)); }
        p("base: " + UUID.baseUUID);

        DataElement e = new DataElement(DataElement.U_INT_1, 255L);
        p("u1: " + e.getDataType() + " " + e.getLong());
        try { new DataElement(DataElement.U_INT_1, 256L); p("u1 256: ok"); } catch (Throwable t) { p("u1 256: " + cn(t)); }
        try { new DataElement(DataElement.INT_1, -129L); p("i1 -129: ok"); } catch (Throwable t) { p("i1 -129: " + cn(t)); }
        p("i4 min: " + new DataElement(DataElement.INT_4, -2147483648L).getLong());
        p("u4 max: " + new DataElement(DataElement.U_INT_4, 4294967295L).getLong());
        try { new DataElement(DataElement.U_INT_4, 4294967296L); p("u4 2^32: ok"); } catch (Throwable t) { p("u4 2^32: " + cn(t)); }
        p("i8: " + new DataElement(DataElement.INT_8, Long.MIN_VALUE).getLong());
        try { new DataElement(DataElement.STRING, 1L); p("string with long: ok"); } catch (Throwable t) { p("string with long: " + cn(t)); }
        p("bool: " + new DataElement(true).getBoolean() + " " + new DataElement(false).getBoolean() + " type " + new DataElement(true).getDataType());
        p("null type: " + new DataElement(DataElement.NULL).getDataType());
        try { new DataElement(DataElement.STRING); p("ctor(STRING): ok"); } catch (Throwable t) { p("ctor(STRING): " + cn(t)); }
        DataElement s = new DataElement(DataElement.STRING, "hi");
        p("string: " + s.getValue() + " " + s.getDataType());
        p("url: " + new DataElement(DataElement.URL, "btspp://x").getValue());
        p("uuid elem: " + ((UUID) new DataElement(DataElement.UUID, u).getValue()).equals(u));
        try { new DataElement(DataElement.UUID, "x"); p("uuid elem with String: ok"); } catch (Throwable t) { p("uuid elem with String: " + cn(t)); }
        try { new DataElement(DataElement.STRING, (Object) null); p("null value: ok"); } catch (Throwable t) { p("null value: " + cn(t)); }
        byte[] b8 = {1, 2, 3, 4, 5, 6, 7, 8};
        DataElement u8 = new DataElement(DataElement.U_INT_8, b8);
        b8[0] = 99;
        p("u8 copy: " + hex(u8.getValue()));
        try { new DataElement(DataElement.U_INT_8, new byte[7]); p("u8 len 7: ok"); } catch (Throwable t) { p("u8 len 7: " + cn(t)); }
        try { new DataElement(DataElement.INT_16, new byte[16]); p("i16: ok"); } catch (Throwable t) { p("i16: " + cn(t)); }
        try { s.getLong(); p("getLong on string: ok"); } catch (Throwable t) { p("getLong on string: " + cn(t)); }
        try { s.getBoolean(); p("getBoolean on string: ok"); } catch (Throwable t) { p("getBoolean on string: " + cn(t)); }
        try { s.getSize(); p("getSize on string: ok"); } catch (Throwable t) { p("getSize on string: " + cn(t)); }
        try { e.getValue(); p("getValue on u1: ok"); } catch (Throwable t) { p("getValue on u1: " + cn(t)); }

        DataElement seq = new DataElement(DataElement.DATSEQ);
        DataElement a = new DataElement(DataElement.U_INT_2, 7L), b = new DataElement(true);
        seq.addElement(a);
        seq.addElement(b);
        seq.insertElementAt(new DataElement(DataElement.NULL), 1);
        p("seq size: " + seq.getSize());
        Enumeration en = seq.getValue() == null ? null : (Enumeration) seq.getValue();
        StringBuffer sb = new StringBuffer();
        while (en.hasMoreElements()) sb.append(((DataElement) en.nextElement()).getDataType()).append(' ');
        p("seq types: " + sb.toString().trim());
        try { seq.insertElementAt(a, 4); p("insert at 4: ok"); } catch (Throwable t) { p("insert at 4: " + cn(t)); }
        try { seq.insertElementAt(a, -1); p("insert at -1: ok"); } catch (Throwable t) { p("insert at -1: " + cn(t)); }
        seq.insertElementAt(a, 3);
        p("insert at size: " + seq.getSize());
        try { seq.addElement(null); p("add null: ok"); } catch (Throwable t) { p("add null: " + cn(t)); }
        p("remove other equal: " + seq.removeElement(new DataElement(DataElement.U_INT_2, 7L)));
        p("remove a: " + seq.removeElement(a) + " size " + seq.getSize());
        p("remove a again: " + seq.removeElement(a) + " size " + seq.getSize());
        try { e.addElement(a); p("add to u1: ok"); } catch (Throwable t) { p("add to u1: " + cn(t)); }
        DataElement alt = new DataElement(DataElement.DATALT);
        p("alt: " + alt.getDataType() + " size " + alt.getSize());
        try { new DataElement(DataElement.STRING + 1); p("ctor(33): ok"); } catch (Throwable t) { p("ctor(33): " + cn(t)); }
        DataElement c = seq.clone();
        c.addElement(new DataElement(DataElement.NULL));
        p("clone is deep copy: " + seq.getSize() + " " + c.getSize());

        DeviceClass dc = new DeviceClass(0x5A020C);
        p("deviceclass: " + Integer.toHexString(dc.getServiceClasses()) + " " + Integer.toHexString(dc.getMajorDeviceClass()) + " " + Integer.toHexString(dc.getMinorDeviceClass()));
        try { new DeviceClass(0x01000000); p("bit 24: ok"); } catch (Throwable t) { p("bit 24: " + cn(t)); }
        p("constants: " + Integer.toHexString(DiscoveryAgent.GIAC) + " " + Integer.toHexString(DiscoveryAgent.LIAC) + " " + DiscoveryAgent.NOT_DISCOVERABLE
                + " " + DiscoveryAgent.CACHED + " " + DiscoveryAgent.PREKNOWN);
        BluetoothConnectionException ex = new BluetoothConnectionException(BluetoothConnectionException.TIMEOUT, "m");
        p("exception: " + ex.getStatus() + " " + ex.getMessage() + " " + (ex instanceof java.io.IOException));
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
