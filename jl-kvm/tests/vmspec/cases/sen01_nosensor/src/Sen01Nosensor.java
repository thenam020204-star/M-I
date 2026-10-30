package vmspec;

import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;
import javax.microedition.sensor.LimitCondition;
import javax.microedition.sensor.MeasurementRange;
import javax.microedition.sensor.ObjectCondition;
import javax.microedition.sensor.RangeCondition;
import javax.microedition.sensor.SensorInfo;
import javax.microedition.sensor.SensorListener;
import javax.microedition.sensor.SensorManager;
import javax.microedition.sensor.Unit;

/** T32 (JSR-256): a device with the Sensor API but no sensor; and the data classes (conditions, ranges, units), which need no sensor. */
public class Sen01Nosensor extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        SensorInfo[] a = SensorManager.findSensors("acceleration", SensorInfo.CONTEXT_TYPE_DEVICE);
        p("findSensors(q, ctx): " + (a == null ? "null" : "length " + a.length));
        a = SensorManager.findSensors("acceleration", null);
        p("findSensors(q, null): " + (a == null ? "null" : "length " + a.length));
        a = SensorManager.findSensors("sensor:temperature;contextType=ambient");
        p("findSensors(url): " + (a == null ? "null" : "length " + a.length));
        try { SensorManager.findSensors(null, null); p("quantity null: ok"); } catch (Throwable t) { p("quantity null: " + cn(t)); }
        try { SensorManager.findSensors((String) null); p("url null: ok"); } catch (Throwable t) { p("url null: " + cn(t)); }
        try { SensorManager.findSensors("temperature"); p("url bad: ok"); } catch (Throwable t) { p("url bad: " + cn(t)); }
        try { Connector.open("sensor:temperature;contextType=ambient"); p("open: opened"); } catch (Throwable t) { p("open: " + cn(t)); }
        SensorListener l = new SensorListener() {
            public void sensorAvailable(SensorInfo info) { p("available"); }
            public void sensorUnavailable(SensorInfo info) { p("unavailable"); }
        };
        SensorManager.addSensorListener(l, "acceleration");
        SensorManager.removeSensorListener(l);
        SensorManager.removeSensorListener(l);
        try { SensorManager.addSensorListener(null, "acceleration"); p("listener null: ok"); } catch (Throwable t) { p("listener null: " + cn(t)); }
        try { SensorManager.addSensorListener(l, (String) null); p("quantity null listener: ok"); } catch (Throwable t) { p("quantity null listener: " + cn(t)); }
        try { SensorManager.removeSensorListener(null); p("remove null: ok"); } catch (Throwable t) { p("remove null: " + cn(t)); }

        LimitCondition lc = new LimitCondition(10.0, LimitCondition.OP_GREATER_THAN_OR_EQUALS);
        p("limit ge 10: " + lc.isMet(9.99) + " " + lc.isMet(10.0) + " " + lc.isMet(11.0) + " object " + lc.isMet((Object) "x") + " " + lc.getLimit() + " " + lc.getOperator());
        p("limit lt/le/gt/eq at 5: " + new LimitCondition(5.0, "lt").isMet(5.0) + " " + new LimitCondition(5.0, "le").isMet(5.0) + " " + new LimitCondition(5.0, "gt").isMet(5.0)
                + " " + new LimitCondition(5.0, "eq").isMet(5.0));
        try { new LimitCondition(1.0, "ne"); p("bad op: ok"); } catch (Throwable t) { p("bad op: " + cn(t)); }
        try { new LimitCondition(1.0, null); p("null op: ok"); } catch (Throwable t) { p("null op: " + cn(t)); }
        RangeCondition rc = new RangeCondition(1.0, RangeCondition.OP_GREATER_THAN, 5.0, RangeCondition.OP_LESS_THAN_OR_EQUALS);
        p("range (1,5]: " + rc.isMet(1.0) + " " + rc.isMet(1.5) + " " + rc.isMet(5.0) + " " + rc.isMet(5.5) + " " + rc.getLowerLimit() + " " + rc.getLowerOp() + " " + rc.getUpperLimit() + " " + rc.getUpperOp());
        try { new RangeCondition(1.0, "lt", 5.0, "le"); p("range bad lower op: ok"); } catch (Throwable t) { p("range bad lower op: " + cn(t)); }
        try { new RangeCondition(6.0, "ge", 5.0, "le"); p("range lower > upper: ok"); } catch (Throwable t) { p("range lower > upper: " + cn(t)); }
        ObjectCondition oc = new ObjectCondition("on");
        p("object: " + oc.isMet((Object) "on") + " " + oc.isMet((Object) "off") + " " + oc.isMet(1.0) + " " + oc.getLimit());
        try { new ObjectCondition(null); p("object null: ok"); } catch (Throwable t) { p("object null: " + cn(t)); }
        MeasurementRange mr = new MeasurementRange(-20.0, 60.0, 0.5);
        p("range: " + mr.getSmallestValue() + " " + mr.getLargestValue() + " " + mr.getResolution());
        try { new MeasurementRange(5.0, 1.0, 0.1); p("range inverted: ok"); } catch (Throwable t) { p("range inverted: " + cn(t)); }
        try { new MeasurementRange(1.0, 5.0, -0.1); p("range neg resolution: ok"); } catch (Throwable t) { p("range neg resolution: " + cn(t)); }
        Unit u = Unit.getUnit("m/s2");
        p("unit: " + u + " same " + (u == Unit.getUnit("m/s2")) + " other " + (u == Unit.getUnit("K")));
        try { Unit.getUnit(null); p("unit null: ok"); } catch (Throwable t) { p("unit null: " + cn(t)); }
        p("constants: " + SensorInfo.CONN_EMBEDDED + SensorInfo.CONN_REMOTE + SensorInfo.CONN_SHORT_RANGE_WIRELESS + SensorInfo.CONN_WIRED + " " + SensorInfo.PROP_MAX_RATE + " "
                + SensorInfo.CONTEXT_TYPE_USER + " " + LimitCondition.OP_EQUALS);
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
