/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal;

/** ROM-private: receives the events of {@link EventLoop} on the event thread (implemented by lcdui.Dispatcher). */
public interface EventHandler {
    void handle(int type, int a, int b, int c);
}
