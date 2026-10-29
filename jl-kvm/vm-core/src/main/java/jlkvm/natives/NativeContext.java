package jlkvm.natives;

import jlkvm.heap.Heap;
import jlkvm.heap.TempRoots;

/** Argument access and result passing for one native call. Parameter index i counts parameters (receiver is 0). */
public interface NativeContext {
    int argInt(int i); long argLong(int i); float argFloat(int i); double argDouble(int i);
    int argRef(int i);                           // guest reference (arena index)
    void retInt(int v); void retLong(long v); void retFloat(float v); void retDouble(double v);
    void retRef(int ref); void retVoid();
    void throwNew(String guestClass, String msg);
    void block(Blocker b);                       // suspends the current guest thread, NOT the vm-thread
    Heap heap(); Handles handles(); TempRoots roots();
}
