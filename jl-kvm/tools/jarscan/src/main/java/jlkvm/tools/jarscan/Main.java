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
        Scanner sc = new Scanner(profile, dbs);
        sc.scan(jar);
        boolean err = sc.hasErrors();

        String md = Report.markdown(jar.getFileName().toString(), profile, sc);
        System.out.print(md);
        if (outDir != null) {
            Files.createDirectories(outDir);
            Map<String, Object> js = new LinkedHashMap<>();
            js.put("jar", jar.toString());
            js.put("profile", profile.name);
            js.put("j2meSafe", !err);
            js.put("stats", sc.stats);
            js.put("findings", sc.findings());
            Files.write(outDir.resolve("report.json"), new GsonBuilder().setPrettyPrinting().create().toJson(js).getBytes(StandardCharsets.UTF_8));
            Files.write(outDir.resolve("report.md"), md.getBytes(StandardCharsets.UTF_8));
        }
        System.exit(err ? 1 : 0);
    }
}
