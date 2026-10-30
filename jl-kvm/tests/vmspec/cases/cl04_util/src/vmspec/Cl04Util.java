package vmspec;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;

public class Cl04Util extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String ex(Runnable r) {
        try { r.run(); return "no-exception"; } catch (Throwable t) { return t.getClass().getName(); }
    }

    static String list(Enumeration e) {
        StringBuffer b = new StringBuffer();
        while (e.hasMoreElements()) { b.append(e.nextElement()).append(','); }
        return b.toString();
    }

    protected void startApp() {
        final Vector v = new Vector();
        v.addElement("a"); v.addElement("b"); v.addElement("c"); v.addElement("b");
        p("vector " + v.size() + " " + v + " " + v.elementAt(2) + " " + v.firstElement() + " " + v.lastElement() + " " + v.indexOf("b") + " " + v.indexOf("b", 2) + " " + v.lastIndexOf("b") + " " + v.lastIndexOf("b", 2) + " " + v.indexOf("z") + " " + v.contains("c") + " " + v.isEmpty());
        v.insertElementAt("X", 1); v.setElementAt("Y", 0);
        p("vector2 " + v + " " + v.removeElement("b") + " " + v.removeElement("q") + " " + v);
        v.removeElementAt(0);
        p("vector3 " + v + " " + list(v.elements()));
        Object[] arr = new Object[6];
        v.copyInto(arr);
        p("copyInto " + arr[0] + arr[1] + arr[2] + arr[3] + arr[4]);
        v.setSize(6);
        p("setSize " + v.size() + " " + v.elementAt(5));
        v.setSize(2);
        p("setSize2 " + v);
        Vector cap = new Vector(5, 3);
        p("capacity " + cap.capacity() + " " + new Vector().capacity() + " " + new Vector(7).capacity());
        v.removeAllElements();
        p("empty " + v.size() + v.isEmpty() + " " + v);
        p("vector ex " + ex(new Runnable() { public void run() { v.elementAt(0); } }) + " " + ex(new Runnable() { public void run() { v.firstElement(); } }) + " " + ex(new Runnable() { public void run() { v.lastElement(); } }) + " " + ex(new Runnable() { public void run() { v.removeElementAt(0); } }) + " " + ex(new Runnable() { public void run() { v.insertElementAt("a", 1); } }) + " " + ex(new Runnable() { public void run() { v.setElementAt("a", 0); } }) + " " + ex(new Runnable() { public void run() { new Vector(-1); } }) + " " + ex(new Runnable() { public void run() { v.setSize(-1); } }) + " " + ex(new Runnable() { public void run() { v.addElement(null); v.elementAt(-1); } }));
        v.addElement(null);
        p("null elem " + v.contains(null) + v.indexOf(null) + v);
        final Stack st = new Stack();
        st.push("x"); st.push("y"); st.push("z");
        p("stack " + st.peek() + " " + st.search("x") + " " + st.search("z") + " " + st.search("q") + " " + st.pop() + " " + st.pop() + " " + st.empty() + " " + st.pop() + " " + st.empty() + " " + st.size());
        p("stack ex " + ex(new Runnable() { public void run() { st.pop(); } }) + " " + ex(new Runnable() { public void run() { st.peek(); } }));
        final Hashtable h = new Hashtable();
        h.put("one", new Integer(1)); h.put("two", new Integer(2)); h.put("three", new Integer(3));
        p("hashtable " + h.size() + " " + h.get("two") + " " + h.get("nine") + " " + h.containsKey("one") + h.containsKey("1") + " " + h.contains(new Integer(3)) + h.contains(new Integer(9)) + " " + h.put("one", new Integer(11)) + " " + h.put("four", new Integer(4)) + " " + h.remove("two") + " " + h.remove("zzz") + " " + h.size() + " " + h.isEmpty());
        int sum = 0, ks = 0;
        for (Enumeration e = h.elements(); e.hasMoreElements();) { sum += ((Integer) e.nextElement()).intValue(); }
        for (Enumeration e = h.keys(); e.hasMoreElements();) { ks += ((String) e.nextElement()).length(); }
        p("hashtable enum " + sum + " " + ks);
        Hashtable big = new Hashtable(2);
        for (int i = 0; i < 500; i++) { big.put(new Integer(i), "v" + i); }
        int ok = 0;
        for (int i = 0; i < 500; i++) { if (("v" + i).equals(big.get(new Integer(i)))) ok++; }
        for (int i = 0; i < 500; i += 2) { big.remove(new Integer(i)); }
        p("hashtable big " + big.size() + " " + ok + " " + big.get(new Integer(499)) + " " + big.get(new Integer(498)));
        big.clear();
        p("cleared " + big.size() + big.isEmpty());
        Hashtable one = new Hashtable(); one.put("k", "v");
        p("hashtable toString " + one + " " + new Hashtable());
        p("hashtable ex " + ex(new Runnable() { public void run() { h.put(null, "x"); } }) + " " + ex(new Runnable() { public void run() { h.put("x", null); } }) + " " + ex(new Runnable() { public void run() { h.get(null); } }) + " " + ex(new Runnable() { public void run() { h.contains(null); } }) + " " + ex(new Runnable() { public void run() { new Hashtable(-1); } }) + " " + ex(new Runnable() { public void run() { new Hashtable().keys().nextElement(); } }) + " " + ex(new Runnable() { public void run() { new Vector().elements().nextElement(); } }));
        Random r = new Random(42);
        p("random " + r.nextInt() + " " + r.nextInt() + " " + r.nextLong() + " " + r.nextInt(100) + " " + r.nextInt(7) + " " + r.nextInt(1 << 20) + " " + r.nextDouble() + " " + r.nextFloat());
        r.setSeed(12345L);
        p("random2 " + r.nextInt(10) + " " + r.nextInt(10) + " " + r.nextInt(10) + " " + r.nextInt(1000000007) + " " + new Random(0).nextInt() + " " + new Random(-1L).nextLong());
        p("random ex " + ex(new Runnable() { public void run() { new Random(1).nextInt(0); } }) + " " + ex(new Runnable() { public void run() { new Random(1).nextInt(-5); } }));
        Date d = new Date(86400000L * 365 + 12345L);
        p("date " + d.getTime() + " " + d.equals(new Date(86400000L * 365 + 12345L)) + " " + d.equals(new Date(5L)) + " " + d.equals(null) + " " + d.hashCode() + " " + new Date(-1L).hashCode());
        d.setTime(1000L);
        p("date2 " + d.getTime());
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        p("tz " + gmt.getID() + " " + gmt.getRawOffset() + " " + gmt.useDaylightTime() + " " + TimeZone.getTimeZone("GMT+02:00").getRawOffset() + " " + TimeZone.getTimeZone("GMT-05:30").getRawOffset() + " " + TimeZone.getTimeZone("UTC").getRawOffset());
        Calendar c = Calendar.getInstance(gmt);
        c.setTime(new Date(1000000000000L));   // 2001-09-09 01:46:40 GMT, a Sunday
        p("cal " + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE) + " " + c.get(Calendar.DAY_OF_MONTH) + " dow=" + c.get(Calendar.DAY_OF_WEEK) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + "." + c.get(Calendar.MILLISECOND) + " ampm=" + c.get(Calendar.AM_PM) + " hour=" + c.get(Calendar.HOUR));
        c.set(Calendar.YEAR, 2000); c.set(Calendar.MONTH, Calendar.FEBRUARY); c.set(Calendar.DAY_OF_MONTH, 29); c.set(Calendar.HOUR_OF_DAY, 23); c.set(Calendar.MINUTE, 59); c.set(Calendar.SECOND, 59);
        p("cal2 " + c.getTime().getTime() + " " + c.get(Calendar.DAY_OF_WEEK));
        c.set(Calendar.DAY_OF_MONTH, 32);
        p("cal3 " + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE) + " " + c.getTime().getTime());
        c.set(Calendar.MONTH, 13);
        p("cal4 " + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE));
        c.set(Calendar.HOUR, 3); c.set(Calendar.AM_PM, Calendar.PM);
        p("cal5 " + c.get(Calendar.HOUR_OF_DAY));
        Calendar e1 = Calendar.getInstance(gmt), e2 = Calendar.getInstance(gmt);
        e1.setTime(new Date(5000L)); e2.setTime(new Date(6000L));
        p("cal cmp " + e1.before(e2) + e2.after(e1) + e1.after(e2) + e1.equals(e2) + e1.equals(e1) + " " + e1.getTimeZone().getID());
        Calendar z = Calendar.getInstance(TimeZone.getTimeZone("GMT+03:00"));
        z.setTime(new Date(0L));
        p("cal tz " + z.get(Calendar.HOUR_OF_DAY) + " " + z.get(Calendar.DATE) + " " + z.getTime().getTime());
        z.setTime(new Date(3600000L * 21)); 
        p("cal tz2 " + z.get(Calendar.HOUR_OF_DAY) + " " + z.get(Calendar.DATE) + " " + z.get(Calendar.DAY_OF_WEEK));
        Calendar neg = Calendar.getInstance(gmt);
        neg.setTime(new Date(-1L));
        p("cal neg " + neg.get(Calendar.YEAR) + "-" + neg.get(Calendar.MONTH) + "-" + neg.get(Calendar.DATE) + " " + neg.get(Calendar.HOUR_OF_DAY) + ":" + neg.get(Calendar.MINUTE) + ":" + neg.get(Calendar.SECOND) + "." + neg.get(Calendar.MILLISECOND) + " dow=" + neg.get(Calendar.DAY_OF_WEEK));
        neg.setTime(new Date(-86400000L * 366 * 100));
        p("cal old " + neg.get(Calendar.YEAR) + "-" + neg.get(Calendar.MONTH) + "-" + neg.get(Calendar.DATE) + " dow=" + neg.get(Calendar.DAY_OF_WEEK));
        neg.setTime(new Date(253402300799000L));
        p("cal far " + neg.get(Calendar.YEAR) + "-" + neg.get(Calendar.MONTH) + "-" + neg.get(Calendar.DATE) + " " + neg.get(Calendar.HOUR_OF_DAY));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
