# Heap layout (PLAN D3, D4; tasks T10, T13)

One `int[]` arena (`jlkvm.heap.Heap.mem`), size = profile heap bytes / 4 (the array is allocated once and never replaced, so
code may cache the reference). A **reference** is the arena index of the object's first word; `0` is null; indices
`0..BASE-1` (`BASE = 2`) are never used. Allocation is a bump pointer (`top`); `freeMemory = (limit - top) * 4`,
`totalMemory = (limit - BASE) * 4`. Everything below `top` is a **parsable sequence of objects**, dead ones included
(headers stay intact), so the collector can walk the heap linearly with `firstObject()/nextObject()/sizeWords()`.

## Object and array

```
object: [h0] [h1] [field words ...]                      size = 2 + instanceWords(classId)
array : [h0] [h1] [length] [element words ...]           size = 3 + dataWords(kind, length)

h0 bits 0..19   class id (arrays have class ids too: "[I", "[Ljava/lang/String;" ...; id 0 is never a class)
   bits 20..23  element kind: 0 object, 1 boolean, 2 byte, 3 char, 4 short, 5 int, 6 float, 7 long, 8 double, 9 reference
   bit  28      reserved for the collector (forwarded / pinned)
   bit  30      FLAG_MONITOR: a Monitor record exists in the monitor table (key = this reference)
   bit  31      FLAG_MARK (GC mark; `mem[ref] < 0` tests it)
h1              identity hash, 0 = not assigned yet; assigned lazily from a seeded xorshift (deterministic, D14), never 0
```

* Instance fields: one word each for boolean/byte/char/short/int/float/reference, **two words (high word first)** for
  long/double. Field `slot` = word offset inside the field area; address = `ref + 2 + slot`. Superclass fields come first.
* Arrays are packed little-endian inside a word: boolean/byte 4 per word, char/short 2 per word, int/float/ref 1,
  long/double 2 words per element (high word first). Padding bits are zero.
* Operand stacks and locals use the same convention (a long/double takes two consecutive words, high first).

## What the collector (T13) needs, and where it is

| Need | Where |
|---|---|
| Size of any object, live or dead | `Heap.sizeWords(ref)`; instance words are registered by the linker with `Heap.setInstanceWords(classId, n)` |
| Reference slots of an instance | `ClassInfo.refSlots` (int[] of field-area word offsets, superclass first) in `jlkvm.link`; reference arrays are `kind == K_REF` (all elements are references); other arrays hold no references |
| Roots: thread stacks | each `VmThread.stack` is an `int[]`; frames are chained through the header words stored after the locals (see `jlkvm.interp.Frames`, header = 7 words: caller fp / method id / pc, SYNC, FLAGS, EXTRA, LOCKS); the reference bitmap of the locals and operand stack of every frame comes from `TypeMaps.at()` for the top frame and `TypeMaps.atCall()` for callers, using the pc saved in the frame header. The header slots `SYNC` (monitor object of a synchronized method) and `EXTRA` (when flagged `F_EXTRA_REF`) are references too |
| Roots: statics | `ClassInfo.statics` (int[]) with `ClassInfo.staticIsRef` (boolean[]) |
| Roots: interned strings, class mirrors, constant-pool string refs | `Vm.internTable()` (String -> ref), `ClassInfo.mirror` (java/lang/Class object, one per class), `ClassInfo.strRefs` (per constant-pool index) |
| Roots: host | `Vm.roots` (`TempRoots`, rewritten in place when objects move), `Vm.handles` (`Handles`: entries with an owner reference), `VmThread.threadObj`, `VmThread.locks[0..lockTop)` (monitorenter locks), `NativeCtx` request fields while a native runs, the preallocated OutOfMemoryError (`Vm.oomThrow`) |
| Roots: monitors | `Monitors` table: key = reference (rekey after compaction, `Monitors.relocate`), wait sets hold thread objects |
| Hash across compaction | the identity hash lives in `h1`, objects are moved with their header: nothing to do |
| WeakReference | class `java/lang/ref/WeakReference` (flag `ClassInfo.isWeakRef`, field `referent`, slot in `ClassInfo.weakSlot`): do not trace through the referent slot, after marking clear referents that are unmarked, enqueue nothing (CLDC 1.1 has no ReferenceQueue) |
| Peer cleanup | `Handles` entries registered with an owner reference: after marking, release the entries whose owner is dead |

Implemented in T13, see `docs/gc.md` (kept for reference). Compaction recommendation (no layout change needed): mark with `FLAG_MARK`, compute forwarding addresses in one linear
walk (running `newTop`), keeping them in a host `int[]` indexed by the object's ordinal or in a sorted break table (the
heap is not touched), update all roots and all reference fields/elements, slide objects down with `System.arraycopy`,
clear marks, `Heap.setTop(newTop)`. Bit 28 of `h0` is free if a forwarding scheme wants a flag. No header word is lost.
