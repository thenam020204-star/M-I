package vmspec;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.Orientation;
import javax.microedition.location.ProximityListener;
import javax.microedition.midlet.MIDlet;

/** T32 (JSR-179): a device WITHOUT a position source but with the Location API: no provider, no last location, no orientation, no proximity monitoring. */
public class Loc01Nohw extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        try { p("getInstance(default criteria): " + LocationProvider.getInstance(new Criteria())); } catch (Throwable t) { p("getInstance(default criteria): " + cn(t)); }
        Criteria c = new Criteria();
        c.setHorizontalAccuracy(10);
        c.setCostAllowed(false);
        c.setAltitudeRequired(true);
        try { p("getInstance(strict criteria): " + LocationProvider.getInstance(c)); } catch (Throwable t) { p("getInstance(strict criteria): " + cn(t)); }
        try { p("getInstance(null): " + LocationProvider.getInstance(null)); } catch (Throwable t) { p("getInstance(null): " + cn(t)); }
        p("last known: " + LocationProvider.getLastKnownLocation());
        try { p("orientation: " + Orientation.getOrientation()); } catch (Throwable t) { p("orientation: " + cn(t)); }
        ProximityListener pl = new ProximityListener() {
            public void monitoringStateChanged(boolean isMonitoringActive) {}
            public void proximityEvent(Coordinates coordinates, Location location) {}
        };
        Coordinates here = new Coordinates(10.0, 20.0, 0.0f);
        try { LocationProvider.addProximityListener(pl, here, 50.0f); p("proximity: ok"); } catch (Throwable t) { p("proximity: " + cn(t)); }
        try { LocationProvider.addProximityListener(null, here, 50.0f); p("proximity null listener: ok"); } catch (Throwable t) { p("proximity null listener: " + cn(t)); }
        try { LocationProvider.addProximityListener(pl, null, 50.0f); p("proximity null coordinates: ok"); } catch (Throwable t) { p("proximity null coordinates: " + cn(t)); }
        try { LocationProvider.addProximityListener(pl, here, 0.0f); p("proximity radius 0: ok"); } catch (Throwable t) { p("proximity radius 0: " + cn(t)); }
        try { LocationProvider.removeProximityListener(pl); p("remove proximity: ok"); } catch (Throwable t) { p("remove proximity: " + cn(t)); }
        try { LocationProvider.removeProximityListener(null); p("remove null: ok"); } catch (Throwable t) { p("remove null: " + cn(t)); }
        Location l = new Location() {};
        p("empty location: valid " + l.isValid() + " coords " + l.getQualifiedCoordinates() + " ts " + l.getTimestamp() + " speed nan " + (l.getSpeed() != l.getSpeed())
                + " course nan " + (l.getCourse() != l.getCourse()) + " method " + l.getLocationMethod() + " addr " + l.getAddressInfo() + " extra " + l.getExtraInfo("text/plain"));
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
