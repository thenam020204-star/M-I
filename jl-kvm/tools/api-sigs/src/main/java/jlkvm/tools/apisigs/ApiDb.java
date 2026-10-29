package jlkvm.tools.apisigs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** API signature database (PLAN T03). Only public/protected surface (D9) of public classes. */
public final class ApiDb {
    public static final int FORMAT = 1;

    public int format = FORMAT;
    public String name;          // e.g. "cldc-1.1"
    public String source;        // provenance of the stub jar(s)
    public String note;          // e.g. UNVERIFIED
    public List<ClassSig> classes = new ArrayList<>();

    public static final class ClassSig {
        public String name;              // internal name, java/lang/Object
        public String superName;         // null for java/lang/Object
        public List<String> interfaces = new ArrayList<>();
        public int access;               // ACC_* of the class (without ACC_SUPER)
        public List<FieldSig> fields = new ArrayList<>();
        public List<MethodSig> methods = new ArrayList<>();
    }

    public static final class FieldSig {
        public String name;
        public String desc;
        public int access;
        public String constant;          // ConstantValue as text (String.valueOf), if any
        public String constantType;      // field descriptor (Z,B,C,S,I,J,F,D or Ljava/lang/String;)
    }

    public static final class MethodSig {
        public String name;
        public String desc;
        public int access;
        public List<String> exceptions = new ArrayList<>();
    }

    private static Gson gson() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    public void write(Path p) throws IOException {
        try (Writer w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            gson().toJson(this, w);
            w.write('\n');
        }
    }

    public static ApiDb read(Path p) throws IOException {
        try (Reader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            return gson().fromJson(r, ApiDb.class);
        }
    }

    public String constKey(FieldSig f) {
        return f.constant == null ? "-" : f.constantType + ":" + f.constant;
    }
}
