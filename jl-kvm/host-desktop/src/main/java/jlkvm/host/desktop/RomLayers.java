package jlkvm.host.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jlkvm.ClassSource;
import jlkvm.DeviceProfile;

/**
 * The guest ROM of a profile (D10): the layer jars named by {@code profile.apis} are searched in that order, a class of a layer the
 * profile does not declare does not exist. Layer name -> jar file: cldc-1.1 / cldc-1.0 -> rom.jar, midp-2.0 -> rom-midp.jar
 * (built by {@code :vm-classlib:buildMidpRom} into vm-classlib/build/rom).
 */
public final class RomLayers implements ClassSource {
    private final List<ClassSource> layers = new ArrayList<ClassSource>();

    public RomLayers(File romDir, DeviceProfile profile) throws IOException {
        for (String api : profile.apis) {
            String f;
            if (api.startsWith("cldc-")) f = "rom.jar";
            else if (api.equals("midp-2.0") || api.equals("midp-2.1")) f = "rom-midp.jar";
            else throw new IOException("no ROM layer for api '" + api + "' yet");
            layers.add(new JarClassSource(new File(romDir, f)));
        }
    }

    public byte[] readClass(String n) {
        for (ClassSource l : layers) { byte[] b = l.readClass(n); if (b != null) return b; }
        return null;
    }

    public byte[] readResource(String p) {
        for (ClassSource l : layers) { byte[] b = l.readResource(p); if (b != null) return b; }
        return null;
    }
}
