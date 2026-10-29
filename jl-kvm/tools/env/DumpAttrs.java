import java.io.*;
import java.util.*;
import java.util.zip.*;

/** Lists class-file version and Code-attribute sub-attributes (e.g. StackMap) of every class in a jar or .class file. */
public class DumpAttrs {
    public static void main(String[] a) throws Exception {
        for (String f : a) {
            if (f.endsWith(".class")) { try (InputStream in = new FileInputStream(f)) { dump(f, in); } }
            else try (ZipFile z = new ZipFile(f)) {
                for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements();) {
                    ZipEntry en = e.nextElement();
                    if (en.getName().endsWith(".class")) try (InputStream in = z.getInputStream(en)) { dump(en.getName(), in); }
                }
            }
        }
    }

    static void dump(String name, InputStream is) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(is));
        in.readInt(); int minor = in.readUnsignedShort(), major = in.readUnsignedShort();
        int n = in.readUnsignedShort();
        String[] utf = new String[n];
        for (int i = 1; i < n; i++) {
            int t = in.readUnsignedByte();
            switch (t) {
                case 1: utf[i] = in.readUTF(); break;
                case 3: case 4: case 9: case 10: case 11: case 12: in.readInt(); break;
                case 5: case 6: in.readLong(); i++; break;
                case 7: case 8: in.readUnsignedShort(); break;
                default: throw new IOException("bad cp tag " + t);
            }
        }
        in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort();
        int ni = in.readUnsignedShort(); for (int i = 0; i < ni; i++) in.readUnsignedShort();
        TreeMap<String, Integer> codeAttrs = new TreeMap<>();
        int nf = in.readUnsignedShort();
        for (int i = 0; i < nf; i++) { in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort(); skipAttrs(in); }
        int nm = in.readUnsignedShort(); int methods = 0, withCode = 0;
        for (int i = 0; i < nm; i++) {
            in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort(); methods++;
            int na = in.readUnsignedShort();
            for (int j = 0; j < na; j++) {
                String an = utf[in.readUnsignedShort()]; int len = in.readInt();
                if ("Code".equals(an)) {
                    withCode++;
                    in.readUnsignedShort(); in.readUnsignedShort();
                    int cl = in.readInt(); in.skipBytes(cl);
                    int ne = in.readUnsignedShort(); in.skipBytes(ne * 8);
                    int nca = in.readUnsignedShort();
                    for (int k = 0; k < nca; k++) {
                        String cn = utf[in.readUnsignedShort()]; int l2 = in.readInt(); in.skipBytes(l2);
                        codeAttrs.merge(cn, 1, Integer::sum);
                    }
                } else in.skipBytes(len);
            }
        }
        System.out.println(name + " version=" + major + "." + minor + " methods=" + methods + " withCode=" + withCode + " codeAttrs=" + codeAttrs);
    }

    static void skipAttrs(DataInputStream in) throws IOException {
        int na = in.readUnsignedShort();
        for (int j = 0; j < na; j++) { in.readUnsignedShort(); in.skipBytes(in.readInt()); }
    }
}
