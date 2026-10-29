# Linking, initialisation and error mapping (PLAN D7, T12)

Sources. `CLDC` = CLDC 1.1 specification text `tools/env/cache/spec/CLDCSpecification1.1.txt` (not committed; line numbers of that
file). `JVMS` = Java Virtual Machine Specification, 2nd edition (1999), the edition CLDC 1.1 refers to; section numbers only
(not in the repo). `oracle` = MicroEmulator 2.0.4 and FreeJ2ME-Plus (HotSpot, PLAN D18 level 4-5, NOT CLDC VMs). Nothing here was
checked on a Nokia KVM or phoneME (PLAN V2, V4): every "UNVERIFIED" below is a decision taken from the spec text.

## 1. Error classes

CLDC only has `java.lang.Error`, `NoClassDefFoundError`, `OutOfMemoryError`, `VirtualMachineError` (CLDC 1853-1859, section
6.2.7.2). For any other error CLDC lets the VM halt in an implementation-specific way **or** throw "the nearest CLDC-supported
superclass of the Error class that must be thrown according to the JVMS" (CLDC 1096-1105 for section 4.2, 1196-1203 for
5.1.4); extra checks the CLDC spec does not demand must use the same rule (CLDC 1107-1110, 1206-1212). The VM throws (never
halts). Implemented once, in `jlkvm.link.Errors.guest(jvmsName)`; every VM-raised error goes through `Errors.error`.

When the class changes, the message starts with the simple JVMS name, e.g. `NoSuchMethodError: A.m()V`, so logs (T35b) and tests
tell the cases apart. `L02Main`/`I05Main`/`I06Main` (vmspec) print that JVMS name; the real class is shown in `# class=` lines.

| JVMS error (where it comes from) | Thrown as | Raised by |
|---|---|---|
| ClassFormatError (bad class file), VerifyError (verifier, illegal opcode), ClassCircularityError | `java.lang.Error` | `Linker.find`, `Interpreter` |
| IncompatibleClassChangeError (getstatic/putstatic vs instance field, invokevirtual/special/static vs method kind, Methodref naming an interface or InterfaceMethodref naming a class, invokeinterface on an object that does not implement the interface, class has an interface as superclass) | `java.lang.Error` | `Linker.resolveMethod/selectInterface/find`, `Interpreter` |
| IllegalAccessError (class, field, method access; superclass/interface not accessible; putfield/putstatic of a final field of another class; invokeinterface selecting a non-public method) | `java.lang.Error` | `Linker.resolveClass/resolveField/resolveMethod/selectInterface/find`, `Interpreter` |
| NoSuchFieldError, NoSuchMethodError, AbstractMethodError, InstantiationError, UnsatisfiedLinkError | `java.lang.Error` | `Linker`, `Interpreter` |
| ExceptionInInitializerError (a `<clinit>` throws a non-Error) | `java.lang.Error`, message `ExceptionInInitializerError: <class>: <message>`, stack trace of the original exception | `Interpreter.unwind` -> `Vm.wrapInitializerError` |
| NoClassDefFoundError (class or superclass/interface missing; every use of a class whose `<clinit>` failed; use of a subclass of such a class) | `java.lang.NoClassDefFoundError` | `Linker.find`, `Vm.initStep` |
| StackOverflowError, InternalError | `java.lang.VirtualMachineError` | `Interpreter` (frame does not fit the thread stack) |
| OutOfMemoryError | `java.lang.OutOfMemoryError`, one preallocated instance (`Vm.oomThrow`) | heap exhausted after the collector ran |
| `Class.forName` of a missing class | `java.lang.ClassNotFoundException` (a *missing superclass* of an existing class stays `NoClassDefFoundError`) | `CoreNatives` |

An Error subclass defined by the application (not by the VM) is thrown unchanged; an Error thrown by `<clinit>` is rethrown as
it is (JVMS 5.5 step 6).

## 2. When errors are raised (lazy linking, D7)

| Event | Time | Spec | Case |
|---|---|---|---|
| Class loaded, parsed, superclass and interfaces loaded, verified | first use of the class: `new`, static member access, `Class.forName`, resolution of a CONSTANT_Class that is *executed* | JVMS 5.3, 5.4; CLDC 5.1.1 (one bootstrap loader) | l01 |
| A class named only in a signature, a field type or an unexecuted branch | never loaded | JVMS 5.4.3 (resolution at first execution) | l01 |
| Missing class | `NoClassDefFoundError` at the executing instruction (catchable there), again on every attempt; not cached | JVMS 5.4.3.1; CLDC 6.2.7.2 | l01 |
| Missing superclass / interface | at the first use of the *subclass*, not before | JVMS 5.3.5 | l01 |
| `checkcast`, `instanceof` with a null operand | the class is **not** resolved | JVMS (instruction descriptions) | l01, l02 26-27 |
| Field / method / class access check | at resolution (first execution), result cached per constant-pool entry | JVMS 5.4.4 | l02 |
| `new` of an abstract class or interface | `InstantiationError` before the class is initialised | JVMS `new` | l02 23-24 |
| Handler class of an exception-table entry | resolved only when an exception reaches that entry; a failure replaces the exception and leaves the method (HotSpot does the same) | JVMS 5.4.3, 7.12 | - (UNVERIFIED on KVM) |
| Application class in `java.*`, `javax.microedition.*`, `com.nokia.mid.*` | refused (not found); ROM classes win anyway (lookup order ROM first) | CLDC 992-1005 (3.4.2.2), 1531-1546 (5.3.3) | l03 |

Access control: public; protected = same package or subclass of the declaring class; package = same package name; private = same
class (no nest-mates in class files <= 49). "Same package" compares package names only, because there is one bootstrap namespace
(CLDC 1164-1169, 5.1.1). Class accessibility: public or same package (arrays: the element class). The `protected` rule on the
receiver type is a verifier rule (T08) and is not repeated at run time.

Method resolution: `<init>` is looked up only in the named class (not inherited); others in the class chain, then the
superinterfaces; `invokevirtual` selection skips private/static methods and an abstract method found first gives
`AbstractMethodError` (it does not fall through to a superclass implementation).

## 3. Class initialisation (`<clinit>`, JVMS 5.5 / 2.17.5)

Triggers: `new`, `getstatic`, `putstatic`, `invokestatic` (declaring class of the member, so an inherited static initialises only the
declaring class), `Class.forName`, `Class.newInstance`, and the class of the static method started by `Vm.launch`. Superclasses are
initialised first, interfaces are not (JVMS 2.17.4). `ClassInfo` states: LINKED -> INITIALIZING (thread id kept) -> INITIALIZED, or ERROR.

- Recursive request by the thread that is initialising: proceeds and sees default / ConstantValue values (case i02).
- Request by another thread: the thread yields and retries until INITIALIZED (proceeds) or ERROR (NoClassDefFoundError) (case i05).
- `<clinit>` ends with an exception: the class becomes ERROR; an Error is rethrown unchanged, any other exception becomes the mapped
  ExceptionInInitializerError (section 1); every later use gives `NoClassDefFoundError`; a subclass of an ERROR class gets the same
  (cases i03, i06). Frame overflow while starting `<clinit>` also marks the class ERROR.
- ConstantValue attributes are stored in the statics just before `<clinit>` runs, for every static field that has one (final or not),
  so `<clinit>` already sees them (cases k02, k04, i04).
- Host-created objects (boot classes, VM exceptions) do not trigger `<clinit>`; a class with a `<clinit>` is left uninitialised so the
  first guest use runs it (instead of never).
- CLDC 5.3.4 (lines 1568-1578, ROMizing): initialisation with a visible effect must happen when the class would first have been loaded;
  ROM classes follow the same rule.

## 4. `invokespecial` and ACC_SUPER

JVMS 2nd ed. 6.4: when the calling class has `ACC_SUPER`, the method is not `<init>` and the reference names a proper superclass of
the calling class, the method is looked up again starting at the **direct superclass**; otherwise the resolved method is called as
resolved. Implemented in `Linker.specialTarget` (cached per class and pool index). The flag is read from the calling class's own
access flags, so a class file from before JDK 1.0.2 (no flag) calls the named method (case k01 -> `A.m`), the same classes with the
flag give `B.m` (case k03).

Oracles: HotSpot treats every class as ACC_SUPER, so MicroEmulator and FreeJ2ME print `B.m` for k01 (marked `diverges:`). PLAN D18 puts
the specification before JVM oracles, so the spec behaviour is the expected one. **UNVERIFIED** on a Nokia KVM/phoneME: if a device is
found to ignore the flag like HotSpot, change `specialTarget` to always look up from the direct superclass and flip k01.

## 5. Differences from the oracles (all recorded in the case `meta.txt`)

| Case | Project VM | Oracles |
|---|---|---|
| i03, i05, i06, l02 | `java.lang.Error` + JVMS name in the message | the JVMS class itself (EIIE, IllegalAccessError, ...) |
| k01 | `A.m` | `B.m` (HotSpot ignores ACC_SUPER) |
| l01 | same | FreeJ2ME cannot run it (adapts every referenced class eagerly) |
| l03 | `javax.microedition.*`, `com.nokia.mid.*` refused | only `java.*` is refused (SecurityException) |

## 6. Open points

Whether a Nokia KVM throws these errors or stops the application (CLDC allows both); whether it performs each access check (CLDC
allows extra checks); the exact timing of verification relative to initialisation (we verify at load, before `<clinit>`); package-private
override rules of JVMS 3rd edition are not implemented (the 2nd edition has none). These need a device or phoneME (V2, V4).
