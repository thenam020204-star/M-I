package jlkvm.host.nokia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Headless {@link NokiaDevice}: writes every request to {@link #log} (tests, host-desktop). */
public class RecordingNokiaDevice implements NokiaDevice {
    public final List<String> log = Collections.synchronizedList(new ArrayList<String>());
    public volatile boolean vibra = true;

    public boolean hasVibra() { return vibra; }
    public void startVibra(int freq, long millis) { log.add("vibra start " + freq + " " + millis); }
    public void stopVibra() { log.add("vibra stop"); }
    public void setLights(int num, int level) { log.add("lights " + num + " " + level); }
    public void flashLights(long millis) { log.add("flash " + millis); }
}
