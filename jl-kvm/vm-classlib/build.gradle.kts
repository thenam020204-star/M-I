// Guest class library (CLDC/MIDP/Nokia, bytecode for the guest VM). NOT compiled by the host javac: tools/romgen compiles it with
// JDK 8 `-source 1.3 -target 1.3` and an empty bootclasspath (D11), preverifies it (ProGuard -microedition), checks that no class
// references anything outside the ROM, runs the project's strict verifier on every class and (T18) diffs the public/protected
// surface against the CLDC 1.1 signature DB (D9). Build fails on any violation (T17).
plugins {
    base
}

val romgen by configurations.creating
dependencies {
    romgen(project(":tools:romgen"))
}

val romJar = layout.buildDirectory.file("rom/rom.jar")

// jlkvm/internal/ = ROM-private helper classes: not part of any device API, excluded from the API diff (documented in docs/classlib.md)
val buildRom = tasks.register<JavaExec>("buildRom") {
    group = "build"
    description = "Compiles vm-classlib/src to the preverified, verified ROM jar (T17)"
    val srcDir = layout.projectDirectory.dir("src")
    val db = rootProject.layout.projectDirectory.file("tools/api-sigs/db/cldc-1.1.json")
    inputs.dir(srcDir)
    inputs.file(db)
    outputs.file(romJar)
    classpath = romgen
    mainClass.set("jlkvm.tools.romgen.RomGen")
    args("--src", srcDir.asFile.path, "--out", romJar.get().asFile.path, "--api-db", db.asFile.path, "--api-ignore", "jlkvm/internal/")
}

tasks.named("assemble") { dependsOn(buildRom) }
tasks.named("check") { dependsOn(buildRom) }

// ---- MIDP 2.0 layer (T21-T23): vm-classlib/midp/src is compiled on top of the CLDC ROM into its OWN jar (rom-midp.jar holds only the
// MIDP classes); the host composes the ROM from the layers a profile declares (D10: profile "apis"). The surface check uses the union
// of the CLDC 1.1 and MIDP 2.0 signature DBs. The classes of the MIDP DB that later tasks write are listed in `pending` (ignored
// on both sides until their task ticks; the list must shrink to nothing by M4 acceptance).
val midpRomJar = layout.buildDirectory.file("rom/rom-midp.jar")
val pending = listOf(
    "javax/microedition/lcdui/Choice", "javax/microedition/lcdui/CustomItem", "javax/microedition/lcdui/DateField", "javax/microedition/lcdui/Form",
    "javax/microedition/lcdui/ImageItem", "javax/microedition/lcdui/ItemStateListener", "javax/microedition/lcdui/List",
    "javax/microedition/lcdui/Spacer", "javax/microedition/lcdui/StringItem", "javax/microedition/lcdui/TextBox", "javax/microedition/lcdui/TextField",   // T24
    "javax/microedition/lcdui/game/",                                                                                                                  // T25
    "javax/microedition/rms/",                                                                                                                         // T26
    "javax/microedition/media/",                                                                                                                       // T27
    "javax/microedition/pki/", "javax/microedition/io/", "java/util/Timer"                                                                              // not assigned to a task of T21-T27 (MIDP GCF part, Timer, pki)
)
val buildMidpRom = tasks.register<JavaExec>("buildMidpRom") {
    group = "build"
    description = "Compiles vm-classlib/midp/src on top of the CLDC ROM into rom-midp.jar (T21-T23)"
    dependsOn(buildRom)
    val srcDir = layout.projectDirectory.dir("midp/src")
    val dbDir = rootProject.layout.projectDirectory.dir("tools/api-sigs/db")
    inputs.dir(srcDir)
    inputs.file(romJar)
    inputs.dir(dbDir)
    outputs.file(midpRomJar)
    classpath = romgen
    mainClass.set("jlkvm.tools.romgen.RomGen")
    val a = mutableListOf("--src", srcDir.asFile.path, "--base", romJar.get().asFile.path, "--delta", "--out", midpRomJar.get().asFile.path,
        "--api-db", dbDir.file("cldc-1.1.json").asFile.path, "--api-db", dbDir.file("midp-2.0.json").asFile.path, "--api-ignore", "jlkvm/internal/")
    pending.forEach { a += listOf("--api-ignore", it) }
    args(a)
}
tasks.named("assemble") { dependsOn(buildMidpRom) }
tasks.named("check") { dependsOn(buildMidpRom) }
