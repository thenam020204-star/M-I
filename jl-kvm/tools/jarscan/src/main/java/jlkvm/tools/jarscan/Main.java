package jlkvm.tools.jarscan;

import com.google.gson.GsonBuilder;

import jlkvm.tools.apisigs.ApiDb;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * jarscan --profile P.json --db-dir DIR [--out-dir OUT] GAME.jar
 * Exit code 1 when any ERROR finding exists (NOT J2ME-SAFE), 0 otherwise.
 */
public final class Main {
    public static void main(String[] args) throws Exception {
        Path profileP = null, dbDir = null, outDir = null, jar = null;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--profile": profileP = Paths.get(args[++i]); break;
                case "--db-dir": dbDir = Paths.get(args[++i]); break;
                case "--out-dir": outDir = Paths.get(args[++i]); break;
                default: jar = Paths.get(args[i]);
            }
        }
        if (profileP == null || dbDir == null || jar == null) {
            System.err.println("usage: jarscan --profile P.json --db-dir DIR [--out-dir OUT] GAME.jar");
            System.exit(2);
        }
        ScanProfile profile = ScanProfile.read(profileP);
        List<ApiDb> dbs = new ArrayList<>();
        for (String a : profile.apis) dbs.add(ApiDb.read(dbDir.resolve(a + ".json")));
        Scanner s = new Scanner(profile, dbs);
        s.scan(jar);
        boolean err = s.hasErrors();

        StringBuilder md = new StringBuilder("# jarscan: " + jar.getFileName() + "\n\nProfile: `" + profile.name + "`\n\n");
        md.append(err ? "**Result: NOT J2ME-SAFE**\n\n" : "**Result: OK**\n\n");
        for (Finding.Level lv : Finding.Level.values()) {
            md.append("## ").append(lv).append("\n\n");
            int n = 0;
            for (Finding f : s.findings()) {
                if (f.level != lv) continue;
                md.append("- `").append(f.code).append("` ").append(f.where).append(": ").append(f.message).append('\n');
                n++;
            }
            if (n == 0) md.append("(none)\n");
            md.append('\n');
        }
        System.out.print(md);
        if (outDir != null) {
            Files.createDirectories(outDir);
            Map<String, Object> js = new LinkedHashMap<>();
            js.put("jar", jar.toString());
            js.put("profile", profile.name);
            js.put("j2meSafe", !err);
            js.put("stats", s.stats);
            js.put("findings", s.findings());
            Files.write(outDir.resolve("report.json"), new GsonBuilder().setPrettyPrinting().create().toJson(js).getBytes(StandardCharsets.UTF_8));
            Files.write(outDir.resolve("report.md"), md.toString().getBytes(StandardCharsets.UTF_8));
        }
        System.exit(err ? 1 : 0);
    }
}
