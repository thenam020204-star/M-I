package jlkvm.heap;

import static org.junit.Assert.*;
import org.junit.Test;

/** T10: read/write of every type, packed arrays, budget accounting, OOM boundary, parsable heap, stable identity hash. */
public class HeapTest {
    @Test
    public void objectFieldsAllTypes() {
        Heap h = new Heap(4096, 1);
        h.setInstanceWords(5, 8);
        int o = h.allocObject(5);
        assertTrue(o >= Heap.BASE);
        assertEquals(5, h.classId(o));
        assertFalse(h.isArray(o));
        h.putInt(o, 0, -7);
        h.putFloat(o, 1, 1.5f);
        h.putLong(o, 2, Long.MIN_VALUE + 12345);
        h.putDouble(o, 4, -2.25);
        h.putRef(o, 6, o);
        assertEquals(-7, h.getInt(o, 0));
        assertEquals(1.5f, h.getFloat(o, 1), 0);
        assertEquals(Long.MIN_VALUE + 12345, h.getLong(o, 2));
        assertEquals(-2.25, h.getDouble(o, 4), 0);
        assertEquals(o, h.getRef(o, 6));
        assertEquals(0, h.getInt(o, 7));
        assertEquals(Heap.HEADER + 8, h.sizeWords(o));
    }

    @Test
    public void packedArrays() {
        Heap h = new Heap(65536, 1);
        int b = h.allocArray(1, Heap.K_BYTE, 7);
        int c = h.allocArray(2, Heap.K_CHAR, 5);
        int s = h.allocArray(3, Heap.K_SHORT, 5);
        int z = h.allocArray(4, Heap.K_BOOLEAN, 9);
        int l = h.allocArray(5, Heap.K_LONG, 3);
        int d = h.allocArray(6, Heap.K_DOUBLE, 3);
        int i = h.allocArray(7, Heap.K_INT, 3);
        int f = h.allocArray(8, Heap.K_FLOAT, 3);
        int r = h.allocArray(9, Heap.K_REF, 3);
        assertEquals(Heap.ARRAY_HEADER + 2, h.sizeWords(b));     // 7 bytes -> 2 words
        assertEquals(Heap.ARRAY_HEADER + 3, h.sizeWords(c));     // 5 chars -> 3 words
        assertEquals(Heap.ARRAY_HEADER + 3, h.sizeWords(z));  // 9 booleans -> 3 words
        assertEquals(Heap.ARRAY_HEADER + 6, h.sizeWords(l));
        for (int k = 0; k < 7; k++) h.putByte(b, k, k % 2 == 0 ? -k - 1 : k + 100);
        for (int k = 0; k < 7; k++) assertEquals((byte) (k % 2 == 0 ? -k - 1 : k + 100), h.getByte(b, k));
        for (int k = 0; k < 5; k++) { h.putChar(c, k, 0xFFF0 + k); h.putShort(s, k, -30000 - k); }
        for (int k = 0; k < 5; k++) { assertEquals((char) (0xFFF0 + k), h.getChar(c, k)); assertEquals((short) (-30000 - k), h.getShort(s, k)); }
        h.putBoolean(z, 8, true);
        assertTrue(h.getBoolean(z, 8));
        assertFalse(h.getBoolean(z, 7));
        h.putLongElem(l, 2, 0x1122334455667788L);
        h.putDoubleElem(d, 1, Math.PI);
        h.putIntElem(i, 2, Integer.MIN_VALUE);
        h.putFloatElem(f, 0, -0.5f);
        h.putRefElem(r, 1, b);
        assertEquals(0x1122334455667788L, h.getLongElem(l, 2));
        assertEquals(0L, h.getLongElem(l, 1));
        assertEquals(Math.PI, h.getDoubleElem(d, 1), 0);
        assertEquals(Integer.MIN_VALUE, h.getIntElem(i, 2));
        assertEquals(-0.5f, h.getFloatElem(f, 0), 0);
        assertEquals(b, h.getRefElem(r, 1));
        assertEquals(3, h.arrayLength(r));
        assertEquals(Heap.K_REF, h.kind(r));
        assertEquals(9, h.classId(r));
        // neighbouring elements are not clobbered by packed stores
        h.putByte(b, 1, 0);
        assertEquals((byte) -1, h.getByte(b, 0));
        assertEquals((byte) -3, h.getByte(b, 2));
    }

    @Test
    public void budgetAndOom() {
        Heap h = new Heap(1024, 1);                // 256 words, 2 reserved
        h.setInstanceWords(1, 0);
        assertEquals(1024 - 8, h.totalMemory());
        assertEquals(h.totalMemory(), h.freeMemory());
        int[] calls = new int[1];
        h.setCollector(new Heap.Collector() { public void collect(Heap hp, int n) { calls[0]++; } });
        long expectFree = h.freeMemory();
        int a = h.allocArray(2, Heap.K_INT, 100);
        expectFree -= (Heap.ARRAY_HEADER + 100) * 4L;
        assertEquals(expectFree, h.freeMemory());
        // fill exactly: remaining words
        int remainingWords = (int) (h.freeMemory() / 4);
        int fill = h.allocArray(2, Heap.K_INT, remainingWords - Heap.ARRAY_HEADER);
        assertTrue(fill != 0);
        assertEquals(0, h.freeMemory());
        assertEquals(0, calls[0]);                  // exact fit needed no collection
        assertEquals(0, h.allocObject(1));          // one word (4 bytes) too much: collector asked, then OOM
        assertEquals(1, calls[0]);
        assertEquals(0, h.allocArray(2, Heap.K_BYTE, 1));
        assertTrue(a != 0);
    }

    @Test
    public void oneByteOverBudget() {
        Heap h = new Heap(4096, 1);
        int[] calls = new int[1];
        h.setCollector(new Heap.Collector() { public void collect(Heap hp, int n) { calls[0]++; } });
        int words = (int) (h.freeMemory() / 4) - Heap.ARRAY_HEADER;   // byte array of 4*words bytes fills heap exactly
        assertEquals(0, h.allocArray(2, Heap.K_BYTE, 4 * words + 1));  // 1 byte too much
        assertEquals(1, calls[0]);
        assertTrue(h.allocArray(2, Heap.K_BYTE, 4 * words) != 0);
        assertEquals(0, h.freeMemory());
    }

    @Test
    public void parsableAndHash() {
        Heap h = new Heap(8192, 7);
        h.setInstanceWords(1, 3);
        int a = h.allocObject(1), b = h.allocArray(2, Heap.K_CHAR, 5), c = h.allocObject(1);
        assertEquals(a, h.firstObject());
        assertEquals(b, h.nextObject(a));
        assertEquals(c, h.nextObject(b));
        assertEquals(0, h.nextObject(c));
        int x = h.identityHash(b);
        assertTrue(x != 0);
        assertEquals(x, h.identityHash(b));
        assertNotEquals(x, h.identityHash(a));
        h.setMarked(a, true);
        assertTrue(h.isMarked(a));
        assertEquals(1, h.classId(a));
        h.setMarked(a, false);
        h.setHasMonitor(a, true);
        assertTrue(h.hasMonitor(a));
        assertEquals(1, h.classId(a));
        // same seed -> same hash sequence (deterministic mode, D14)
        Heap h2 = new Heap(8192, 7);
        h2.setInstanceWords(1, 3);
        int a2 = h2.allocObject(1), b2 = h2.allocArray(2, Heap.K_CHAR, 5);
        assertEquals(x, h2.identityHash(b2));
    }
}
