# Garbage collector (PLAN D4, D5; task T13)

`vm-core/src/main/java/jlkvm/gc/Gc.java`. Precise, stop-the-world, **mark + sliding compaction** over the arena of
`docs/heap-layout.md`. There is one collector run at a time, on the vm-thread; no other code touches the heap meanwhile (D5), so
"stop the world" costs nothing. Free space is always one contiguous block (`Heap.top()`..`limit`), so there is no fragmentation and
no free list.

## When it runs (safepoints, D5)

`Heap.reserve` calls the collector when an allocation does not fit (and `Runtime.gc()` calls it directly). Both happen only where the
state of the allocating thread is published:

* **Interpreter allocation instructions** (`new`, `newarray`, `anewarray`, `multianewarray`, `ldc` of a string/class literal, the class
  mirror of a static synchronized call): the interpreter writes `fp/sp/pc/method` into the `VmThread` *before* allocating, with `pc`
  = the allocating instruction (nothing of it has been executed), so the frame is described by `TypeMaps.at(method, pc)`.
  A collection never starts between two arbitrary bytecodes and never inside an instruction that has already modified its operands.
* **Natives and VM helpers** (`Vm.newString`, `Vm.newThrowable`, `Class.newInstance`, host natives): the interpreter published the
  registers and set `VmThread.inNative` (the native's arguments are still on the operand stack; their reference-ness comes from the
  method descriptor, `RMethod.argWordIsRef`). Host code keeps references it needs across an allocation in `TempRoots`
  (`roots.push` / re-read with `roots.get` after the allocation) or in a handle; a raw `int` reference held in a Java local is
  invalid after any call that can allocate.
* **Other green threads** are always parked with their registers published (they only run inside `Scheduler.runSlice`), so they
  need no special treatment. Their top-frame pc is "next instruction" (preempted / switched), "the instruction to retry"
  (class initialisation) or "after the invoke" (blocked native, sleep, wait): the map is `TypeMaps.at` in all of them; slots above
  the real `sp` (a blocked native has not pushed its result yet) are ignored.

Debug: `VmConfig.gcStressInterval = N` (RunJar `--gc-stress N`) collects before every N-th allocation and `gcVerify`
(`--gc-verify`) checks that every root and every reference is an object start and re-walks the heap afterwards.
`JLKVM_ARGS="--gc-stress 1 --gc-verify" tests/vmspec/run_vmspec.sh --engine jlkvm` runs the whole corpus that way.

## Algorithm

1. **Mark.** Roots (below) are marked with `FLAG_MARK` (bit 31 of the header word); an explicit stack avoids recursion.
   Instances are traced through `ClassInfo.refSlots`, reference arrays through all elements; primitive arrays hold nothing.
   `WeakReference` objects (`ClassInfo.isWeakRef`, slot `weakSlot` = field `referent`) are traced except for the referent
   and remembered.
2. **Weak references / peers / monitors.** Referents that are unmarked are set to null. Host handles (`Handles`) whose owner
   object is unmarked are released and, if the host object implements `jlkvm.natives.Disposable`, disposed (peer cleanup, D8).
   Monitor records of dead objects are dropped.
3. **Forwarding table.** One linear walk of the heap (parsable: dead objects keep their headers) collects *runs* of consecutive
   live objects: old start, new start, length. `forward(ref)` is a binary search in this table. Nothing is written into the heap.
4. **Rewrite.** If anything moves, the root pass runs again in "update" mode (every root slot is replaced by `forward(slot)`,
   `Monitors.relocate` re-keys the monitor table, handle owners are forwarded), then every live object's reference fields/
   elements are rewritten while it is still at its old address.
5. **Slide.** Runs are copied down with `System.arraycopy` (increasing addresses, overlap safe), marks are cleared during step 4,
   `Heap.setTop(newTop)`. The identity hash lives in the header (`h1`) and moves with the object, so it never changes.

## Roots

| Root | Source |
|---|---|
| Thread frames | per thread: walk from `fp/pc/method` down the frame chain (`Frames`); each frame: locals + operand stack by `TypeMaps` (`at` for the top frame and for callers of class-initialisation frames, `atCall` for callers inside an invoke), header `SYNC`, `EXTRA` if `F_EXTRA_REF`; the native in progress adds its argument words |
| Thread fields | `threadObj`, `waitRef`, `locks[0..lockTop)` (monitorenter locks) |
| Statics | `ClassInfo.statics` where `staticIsRef` |
| Class data | `ClassInfo.mirror`, `ClassInfo.strRefs` (string literals of the constant pool) |
| Interned strings | `Vm.internTable()` values (strong, as in PLAN) |
| Host | `TempRoots`, `Vm.hostRootCells()` (the preallocated OutOfMemoryError) |
| Weak | handle owners, WeakReference referents, monitor keys |

**Dead locals are not roots.** The reference map of a frame comes from the verifier state at that pc, which for a preverified class is
the `StackMap` frames; the preverifier omits locals that are not used afterwards. Such a local may therefore be collected while the
method is still running (like a JIT's liveness maps). A method must read a variable after the `gc()` if it needs the object.

**No GC without a type map.** A method that does not verify (`--noverify`, `jsr/ret`, lenient failures) has no map; a collection
that needs it throws `IllegalStateException("GC needs a verified method ...")`. Strict mode (the default and the only release mode,
D6) always has maps.

## Failure and limits

* After a collection the allocation is retried; still short -> `0` -> guest `OutOfMemoryError` (the preallocated instance,
  case g06). The collector itself needs no guest memory; its tables (mark stack, run table) are host memory and are not counted
  against the heap budget (D3 counts only the arena).
* `Gc` statistics (`collections`, `lastNanos`, `totalNanos`, `maxNanos`, `lastLiveWords`, `lastFreedWords`) feed T16/T36.
  Measured on this container (JDK 8 desktop, 3 MB heap, the 1M-allocation stress): 26 collections, max pause about 8-20 ms,
  total about 130 ms.
* A `Blocker` (natives/Blocker) must not hold a raw guest reference across a collection; use `Handles` or `TempRoots`.
* Interned strings are never collected; classes are never unloaded.
