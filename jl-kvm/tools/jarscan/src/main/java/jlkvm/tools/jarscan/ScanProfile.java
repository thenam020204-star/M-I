package jlkvm.tools.jarscan;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** What the scanned jar is checked against. */
public final class ScanProfile {
    public String name;
    public List<String> apis = new ArrayList<>();   // names of api-sigs DBs, resolved in --db-dir
    public int maxClassVersion = 49;
    public boolean floatingPoint = true;
    public boolean requireStackMap = true;
    public int largeArrayThreshold = 8192;
    public String source;

    /** The caller closes {@code r}. */
    public static ScanProfile read(Reader r) {
        return new Gson().fromJson(r, ScanProfile.class);
    }

    public static ScanProfile read(Path p) throws IOException {
        try (Reader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            return new Gson().fromJson(r, ScanProfile.class);
        }
    }
}
