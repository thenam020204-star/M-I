package jlkvm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VmInfoTest {
    @Test
    public void configurationIsSpelledCorrectly() {
        // JL-Mod shipped the typo "CDLC-1.1"; PLAN requires CLDC-1.1.
        assertEquals("CLDC-1.1", VmInfo.TARGET_CONFIGURATION);
    }
}
