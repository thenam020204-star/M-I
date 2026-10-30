package jlkvm.host.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import jlkvm.host.HostEvents;
import org.junit.Test;

/** The event queue of the widget host (PLAN T35c): one UI_* event per user action, text mailbox, no guest call. */
public class UserActionsTest {
    private final List<String> events = new ArrayList<String>();
    private final UserActions ua = new UserActions(new UserActions.Sink() {
        public void post(int type, int a, int b, int c) { events.add(type + ":" + a + "," + b + "," + c); }
    });

    @Test public void commandsAndChoices() {
        ua.command(7, 70);
        ua.itemCommand(31, 5);
        ua.choice(12, 2, true);
        ua.choice(3, 0, false);
        assertEquals("[" + HostEvents.UI_COMMAND + ":7,70,0, " + HostEvents.UI_ITEM_COMMAND + ":31,5,0, " + HostEvents.UI_CHOICE + ":12,2,1, "
                + HostEvents.UI_CHOICE + ":3,0,0]", events.toString());
    }

    @Test public void textMailboxLatestWins() {
        assertNull(ua.text(9));
        ua.text(9, "a");
        ua.text(9, "ab");
        ua.text(10, null);
        assertEquals("ab", ua.text(9));
        assertEquals("", ua.text(10));
        assertEquals(3, events.size());
        assertEquals(HostEvents.UI_TEXT + ":9,0,0", events.get(0));
    }

    @Test public void gaugeDateCustom() {
        ua.gauge(4, 6);
        ua.date(5, 0x123456789ABCL);
        ua.dateCleared(5);
        ua.custom(8, 32, UserActions.xy(3, -2));
        assertEquals(HostEvents.UI_GAUGE + ":4,6,0", events.get(0));
        assertEquals(HostEvents.UI_DATE + ":5,4660,1450744508", events.get(1));
        assertEquals(HostEvents.UI_DATE_CLEAR + ":5,0,0", events.get(2));
        assertEquals(HostEvents.UI_CUSTOM + ":8,32,262142", events.get(3));
    }
}
