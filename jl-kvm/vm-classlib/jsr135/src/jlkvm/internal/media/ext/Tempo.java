/*
 * jl-kvm guest class library (JSR-135 layer, profile api "jsr-135"). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 * API signatures: tools/api-sigs/db/jsr-135-extra.json (extracted from the JL-Mod classes, Apache-2.0; docs/midp.md section 10).
 */
package jlkvm.internal.media.ext;

import javax.microedition.media.control.TempoControl;
import jlkvm.internal.media.BasePlayer;

/**
 * TempoControl / RateControl. Rate is a milli-percentage (100000 = normal), tempo milli-beats per minute. Out-of-range values are
 * clamped to the supported range (UNVERIFIED: the javadoc says "closest supported value"); a tone sequence player scales its notes,
 * a clip player asks the host.
 */
final class Tempo implements TempoControl {
    private final BasePlayer p;

    Tempo(BasePlayer p) {
        this.p = p;
    }

    public int setRate(int millirate) {
        return p.setRateMilli(millirate);
    }

    public int getRate() {
        return p.getRateMilli();
    }

    public int getMaxRate() {
        return p.maxRateMilli();
    }

    public int getMinRate() {
        return p.minRateMilli();
    }

    public int setTempo(int millitempo) {
        return p.setTempoMilli(millitempo);
    }

    public int getTempo() {
        return p.getTempoMilli();
    }
}
