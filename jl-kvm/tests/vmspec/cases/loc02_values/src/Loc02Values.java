package vmspec;

import java.util.Enumeration;
import javax.microedition.location.AddressInfo;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Landmark;
import javax.microedition.location.LandmarkException;
import javax.microedition.location.LandmarkStore;
import javax.microedition.location.Orientation;
import javax.microedition.location.QualifiedCoordinates;
import javax.microedition.midlet.MIDlet;

/** T32 (JSR-179): the data classes, which need no position source. */
public class Loc02Values extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static int count(Enumeration e) { int n = 0; while (e != null && e.hasMoreElements()) { e.nextElement(); n++; } return n; }

    protected void startApp() {
        Coordinates a = new Coordinates(0.0, 0.0, 5.0f);
        p("getters: " + a.getLatitude() + " " + a.getLongitude() + " " + a.getAltitude());
        try { new Coordinates(90.5, 0.0, 0f); p("lat 90.5: ok"); } catch (Throwable t) { p("lat 90.5: " + cn(t)); }
        try { new Coordinates(0.0, 180.0, 0f); p("lon 180: ok"); } catch (Throwable t) { p("lon 180: " + cn(t)); }
        try { new Coordinates(0.0, -180.0, 0f); p("lon -180: ok"); } catch (Throwable t) { p("lon -180: " + cn(t)); }
        try { new Coordinates(Double.NaN, 0.0, 0f); p("lat NaN: ok"); } catch (Throwable t) { p("lat NaN: " + cn(t)); }
        a.setLatitude(45.25);
        a.setLongitude(-120.5);
        a.setAltitude(Float.NaN);
        p("setters: " + a.getLatitude() + " " + a.getLongitude() + " nan " + (a.getAltitude() != a.getAltitude()));
        try { a.setLatitude(-91.0); p("set lat -91: ok"); } catch (Throwable t) { p("set lat -91: " + cn(t)); }
        p("lat unchanged after error: " + a.getLatitude());

        p("convert 12.5 DD_MM_SS: " + Coordinates.convert(12.5, Coordinates.DD_MM_SS));
        p("convert -12.5 DD_MM: " + Coordinates.convert(-12.5, Coordinates.DD_MM));
        p("convert 0: " + Coordinates.convert(0.0, Coordinates.DD_MM_SS));
        p("convert 179.999 DD_MM_SS: " + Coordinates.convert(179.999, Coordinates.DD_MM_SS));
        p("convert 1/3 DD_MM_SS: " + Coordinates.convert(1.0 / 3.0, Coordinates.DD_MM_SS));
        try { Coordinates.convert(181.0, 1); p("convert 181: ok"); } catch (Throwable t) { p("convert 181: " + cn(t)); }
        try { Coordinates.convert(1.0, 3); p("convert type 3: ok"); } catch (Throwable t) { p("convert type 3: " + cn(t)); }
        String[] in = {"12:30:00.000", "-12:30.000", "45", "45.25", "-0:30:00", "12:30", "12:60", "12:30:60", "a", "", "1:2:3:4", ":", "12:.5", "12.5:30"};
        for (int i = 0; i < in.length; i++) {
            try { p("parse '" + in[i] + "': " + Coordinates.convert(in[i])); } catch (Throwable t) { p("parse '" + in[i] + "': " + cn(t)); }
        }
        try { Coordinates.convert((String) null); p("parse null: ok"); } catch (Throwable t) { p("parse null: " + cn(t)); }

        Coordinates o = new Coordinates(0.0, 0.0, 0f);
        p("distance 1 deg lon: " + (int) o.distance(new Coordinates(0.0, 1.0, 0f)) + " lat: " + (int) o.distance(new Coordinates(1.0, 0.0, 0f)) + " self: " + (int) o.distance(o));
        p("distance london-paris km: " + (int) (new Coordinates(51.5074, -0.1278, 0f).distance(new Coordinates(48.8566, 2.3522, 0f)) / 1000.0f));
        p("azimuth e/n/w/s: " + (int) (o.azimuthTo(new Coordinates(0.0, 1.0, 0f)) + 0.5f) + " " + (int) (o.azimuthTo(new Coordinates(1.0, 0.0, 0f)) + 0.5f) + " "
                + (int) (o.azimuthTo(new Coordinates(0.0, -1.0, 0f)) + 0.5f) + " " + (int) (o.azimuthTo(new Coordinates(-1.0, 0.0, 0f)) + 0.5f));
        try { o.distance(null); p("distance null: ok"); } catch (Throwable t) { p("distance null: " + cn(t)); }

        QualifiedCoordinates q = new QualifiedCoordinates(1.0, 2.0, 3.0f, 4.0f, 5.0f);
        p("qualified: " + q.getLatitude() + " " + q.getLongitude() + " " + q.getAltitude() + " " + q.getHorizontalAccuracy() + " " + q.getVerticalAccuracy() + " is Coordinates "
                + (q instanceof Coordinates));
        p("qualified equals: " + q.equals(new QualifiedCoordinates(1.0, 2.0, 3.0f, 4.0f, 5.0f)) + " " + q.equals(new QualifiedCoordinates(1.0, 2.0, 3.0f, 4.0f, 6.0f)) + " " + q.equals(null));
        try { q.setHorizontalAccuracy(-1.0f); p("hacc -1: ok"); } catch (Throwable t) { p("hacc -1: " + cn(t)); }
        q.setVerticalAccuracy(Float.NaN);
        p("vacc nan ok: " + (q.getVerticalAccuracy() != q.getVerticalAccuracy()));

        Criteria c = new Criteria();
        p("criteria defaults: " + c.getHorizontalAccuracy() + " " + c.getVerticalAccuracy() + " " + c.getPreferredResponseTime() + " " + c.getPreferredPowerConsumption() + " cost "
                + c.isAllowedToCost() + " speed " + c.isSpeedAndCourseRequired() + " alt " + c.isAltitudeRequired() + " addr " + c.isAddressInfoRequired());
        c.setHorizontalAccuracy(30); c.setVerticalAccuracy(40); c.setPreferredResponseTime(5000); c.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM);
        c.setCostAllowed(false); c.setSpeedAndCourseRequired(true); c.setAltitudeRequired(true); c.setAddressInfoRequired(true);
        p("criteria set: " + c.getHorizontalAccuracy() + " " + c.getVerticalAccuracy() + " " + c.getPreferredResponseTime() + " " + c.getPreferredPowerConsumption() + " cost "
                + c.isAllowedToCost() + " speed " + c.isSpeedAndCourseRequired() + " alt " + c.isAltitudeRequired() + " addr " + c.isAddressInfoRequired());
        try { c.setPreferredPowerConsumption(4); p("power 4: ok"); } catch (Throwable t) { p("power 4: " + cn(t)); }

        AddressInfo ai = new AddressInfo();
        p("address empty: " + ai.getField(AddressInfo.CITY));
        ai.setField(AddressInfo.CITY, "Hue");
        ai.setField(AddressInfo.PHONE_NUMBER, "123");
        p("address: " + ai.getField(AddressInfo.CITY) + " " + ai.getField(AddressInfo.PHONE_NUMBER) + " " + AddressInfo.EXTENSION + " " + AddressInfo.URL);
        try { ai.getField(0); p("field 0: ok"); } catch (Throwable t) { p("field 0: " + cn(t)); }
        try { ai.setField(18, "x"); p("field 18: ok"); } catch (Throwable t) { p("field 18: " + cn(t)); }

        Orientation or = new Orientation(90.0f, true, 10.0f, -20.0f);
        p("orientation: " + (int) or.getCompassAzimuth() + " " + or.isOrientationMagnetic() + " " + (int) or.getPitch() + " " + (int) or.getRoll());
        try { new Orientation(360.0f, true, 0f, 0f); p("azimuth 360: ok"); } catch (Throwable t) { p("azimuth 360: " + cn(t)); }
        try { new Orientation(0f, true, 91f, 0f); p("pitch 91: ok"); } catch (Throwable t) { p("pitch 91: " + cn(t)); }

        try { new Landmark(null, "d", null, null); p("landmark null name: ok"); } catch (Throwable t) { p("landmark null name: " + cn(t)); }
        Landmark lm = new Landmark("Home", "my place", new QualifiedCoordinates(10.0, 20.0, 0f, 1f, 1f), null);
        Landmark far = new Landmark("Far", null, new QualifiedCoordinates(-40.0, 100.0, 0f, 1f, 1f), ai);
        p("landmark: " + lm.getName() + " " + lm.getDescription() + " " + lm.getAddressInfo() + " " + far.getAddressInfo().getField(AddressInfo.CITY));

        try {
            LandmarkStore def = LandmarkStore.getInstance(null);
            p("default store: " + (def != null) + " same " + (def == LandmarkStore.getInstance(null)));
            p("store x before: " + LandmarkStore.getInstance("x"));
            LandmarkStore.createLandmarkStore("x");
            p("store x after: " + (LandmarkStore.getInstance("x") != null));
            try { LandmarkStore.createLandmarkStore("x"); p("create again: ok"); } catch (LandmarkException e) { p("create again: LandmarkException"); }
            String[] names = LandmarkStore.listLandmarkStores();
            p("stores: " + names.length + " " + names[0] + " " + names[1]);
            LandmarkStore s = LandmarkStore.getInstance("x");
            p("empty getLandmarks: " + s.getLandmarks());
            s.addCategory("Places");
            s.addCategory("Places");
            p("categories: " + count(s.getCategories()));
            s.addLandmark(lm, "Places");
            s.addLandmark(far, null);
            try { s.addLandmark(lm, "Nope"); p("add to unknown category: ok"); } catch (Throwable t) { p("add to unknown category: " + cn(t)); }
            p("all: " + count(s.getLandmarks()) + " in Places: " + count(s.getLandmarks("Places", null)) + " by name: " + count(s.getLandmarks(null, "Far")) + " none: " + s.getLandmarks(null, "Nobody"));
            p("box: " + count(s.getLandmarks(null, 0.0, 20.0, 10.0, 30.0)) + " " + count(s.getLandmarks(null, -50.0, -30.0, 90.0, 110.0)) + " " + s.getLandmarks("Places", -50.0, -30.0, 90.0, 110.0));
            try { s.getLandmarks(null, 20.0, 0.0, 10.0, 30.0); p("bad box: ok"); } catch (Throwable t) { p("bad box: " + cn(t)); }
            lm.setName("Home2");
            s.updateLandmark(lm);
            p("updated: " + count(s.getLandmarks(null, "Home2")));
            s.removeLandmarkFromCategory(lm, "Places");
            p("after remove from category: " + s.getLandmarks("Places", null));
            s.deleteLandmark(far);
            try { s.deleteLandmark(far); p("delete twice: ok"); } catch (LandmarkException e) { p("delete twice: LandmarkException"); }
            try { s.updateLandmark(far); p("update deleted: ok"); } catch (LandmarkException e) { p("update deleted: LandmarkException"); }
            s.deleteCategory("Places");
            p("categories after delete: " + count(s.getCategories()));
            LandmarkStore.deleteLandmarkStore("x");
            p("store x deleted: " + LandmarkStore.getInstance("x"));
            try { LandmarkStore.deleteLandmarkStore("x"); p("delete store twice: ok"); } catch (LandmarkException e) { p("delete store twice: LandmarkException"); }
        } catch (Throwable t) {
            p("unexpected: " + t);
        }
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
