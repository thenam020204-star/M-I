package jlkvm.tools.jarscan;

/** The jarscan report as Markdown (shared by the CLI and the Android installer, PLAN T34). */
public final class Report {
    private Report() {}

    public static String markdown(String jarName, ScanProfile profile, Scanner s) {
        boolean err = s.hasErrors();
        StringBuilder md = new StringBuilder("# jarscan: " + jarName + "\n\nProfile: `" + profile.name + "`\n\n");
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
        return md.toString();
    }
}
