# Error mapping (PLAN D7; skeleton written in T11, completed by T12)

Rule (CLDC 1.1 spec 4.2 and 5.1.4, `tools/env/cache/spec/CLDCSpecification1.1.txt`, not committed): CLDC only defines
`java.lang.Error`, `NoClassDefFoundError`, `OutOfMemoryError` and `VirtualMachineError` (spec 6.2.7.2). An Error of the
Java SE hierarchy that is not in this list is replaced by its nearest CLDC superclass. Implemented in
`jlkvm.link.Errors.guest(jvmsName)`; every VM-raised error goes through it.

| JVMS error | Thrown as | Where |
|---|---|---|
| VerifyError, ClassFormatError, ClassCircularityError, IncompatibleClassChangeError, AbstractMethodError, InstantiationError, NoSuchFieldError, NoSuchMethodError, IllegalAccessError, UnsatisfiedLinkError, LinkageError | `java.lang.Error` | Linker, Interpreter |
| ExceptionInInitializerError (a `<clinit>` throws a non-Error) | `java.lang.Error` (message = class name of the original exception) | Interpreter.unwind |
| NoClassDefFoundError (missing class; also every use of a class whose `<clinit>` failed, and of a subclass of it) | `java.lang.NoClassDefFoundError` | Linker.find, Vm.initStep |
| StackOverflowError, InternalError | `java.lang.VirtualMachineError` | Interpreter (frame does not fit the thread stack) |
| OutOfMemoryError | `java.lang.OutOfMemoryError`, one preallocated instance (`Vm.oomThrow`) | Heap exhausted after the collector ran |
| `Class.forName` of a missing class | `java.lang.ClassNotFoundException` | CoreNatives |

Open points (UNVERIFIED on a Nokia KVM, V2/V4): whether the device throws these or stops the application (CLDC 5.1.4 allows
both); `ACC_SUPER`: `invokespecial` is always executed with the ACC_SUPER semantics (vmspec k01 expects `B.m`); the timing of
linkage errors is "first execution of the instruction" for field/method resolution and "first use of the class" for
loading+verification (T12 will write the oracle comparison).
