import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.io.*;
import java.nio.file.*;

/** Peephole <classes-dir>: xoá checkcast T liền sau checkcast T (không vượt label); ghi lại bằng ClassWriter(0). */
public class Peephole {
    public static void main(String[] a) throws Exception {
        final int[] removed = {0};
        Files.walk(Paths.get(a[0])).filter(p -> p.toString().endsWith(".class")).forEach(p -> {
            try {
                byte[] b = Files.readAllBytes(p);
                ClassNode cn = new ClassNode();
                new ClassReader(b).accept(cn, 0);
                boolean changed = false;
                for (MethodNode m : cn.methods) {
                    if (m.instructions == null) continue;
                    for (AbstractInsnNode in = m.instructions.getFirst(); in != null; ) {
                        AbstractInsnNode nx = in.getNext();
                        if (in.getOpcode() == Opcodes.CHECKCAST && nx != null && nx.getOpcode() == Opcodes.CHECKCAST
                                && ((TypeInsnNode) in).desc.equals(((TypeInsnNode) nx).desc)) {
                            m.instructions.remove(nx); removed[0]++; changed = true; continue;
                        }
                        in = nx;
                    }
                }
                if (changed) {
                    ClassWriter cw = new ClassWriter(0);
                    cn.accept(cw);
                    Files.write(p, cw.toByteArray());
                }
            } catch (IOException e) { throw new UncheckedIOException(e); }
        });
        System.out.println("peephole: removed " + removed[0] + " duplicate checkcast");
    }
}
