/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms;

import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

/**
 * A device with the AMMS API but without any of its services: the entry points answer what the javadoc says for "not supported" - the factories
 * throw MediaException, the lookups give null or empty arrays. The effects, 3D audio, camera and image processing of a real device would be
 * registered by a host (Android M6, NOT done); no Control or Module implementation exists in this ROM.
 */
public final class GlobalManager {
    private GlobalManager() {
    }

    public static Spectator getSpectator() throws MediaException {
        throw new MediaException("3D audio is not supported on this device");
    }

    public static SoundSource3D createSoundSource3D() throws MediaException {
        throw new MediaException("3D audio is not supported on this device");
    }

    public static EffectModule createEffectModule() throws MediaException {
        throw new MediaException("effect modules are not supported on this device");
    }

    public static MediaProcessor createMediaProcessor(String contentType) throws MediaException {
        if (contentType == null) {
            throw new NullPointerException("contentType");
        }
        throw new MediaException("media processing of " + contentType + " is not supported on this device");
    }

    public static String[] getSupportedSoundSource3DPlayerTypes() {
        return new String[0];
    }

    public static String[] getSupportedMediaProcessorInputTypes() {
        return new String[0];
    }

    /** Null: this device supports no global control. */
    public static Control getControl(String controlType) {
        if (controlType == null) {
            throw new NullPointerException("controlType");
        }
        return null;
    }

    public static Control[] getControls() {
        return new Control[0];
    }
}
