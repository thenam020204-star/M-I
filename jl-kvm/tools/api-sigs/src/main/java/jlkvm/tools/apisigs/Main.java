package jlkvm.tools.apisigs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * api-sigs extract --name N [--source S] [--note T] -o OUT.json JAR...
 * api-sigs diff [--ignore INTERNAL_NAME_PREFIX]... A.json B.json   (exit 1 when different)
 * api-sigs count DB.json
 */
public final class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) usage();
        switch (args[0]) {
            case "extract": {
                String name = null, source = "", note = null, out = null;
                List<Path> jars = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    switch (args[i]) {
                        case "--name": name = args[++i]; break;
                        case "--source": source = args[++i]; break;
                        case "--note": note = args[++i]; break;
                        case "-o": out = args[++i]; break;
                        default: jars.add(Paths.get(args[i]));
                    }
                }
                if (name == null || out == null || jars.isEmpty()) usage();
                ApiDb db = Extractor.extract(name, source, note, jars.toArray(new Path[0]));
                db.write(Paths.get(out));
                System.out.println(out + ": " + db.classes.size() + " classes");
                break;
            }
            case "diff": {
                List<String> files = new ArrayList<>(), ignore = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    if (args[i].equals("--ignore")) ignore.add(args[++i]);
                    else files.add(args[i]);
                }
                if (files.size() != 2) usage();
                List<String> d = Differ.diff(ApiDb.read(Paths.get(files.get(0))), ApiDb.read(Paths.get(files.get(1))), ignore.toArray(new String[0]));
                for (String s : d) System.out.println(s);
                System.out.println(d.size() + " difference(s)");
                System.exit(d.isEmpty() ? 0 : 1);
                break;
            }
            case "count": {
                System.out.println(ApiDb.read(Paths.get(args[1])).classes.size());
                break;
            }
            default: usage();
        }
    }

    private static void usage() {
        System.err.println("usage: api-sigs extract --name N [--source S] [--note T] -o OUT.json JAR... | diff [--ignore PREFIX]... A.json B.json | count DB.json");
        System.exit(2);
    }
}
