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
val pending = listOf<String>()   // empty since T27: the whole MIDP 2.0 DB is in the ROM
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

// ---- JSR-135 layer (T27, D10: profile api "jsr-135"): MIDIControl / RateControl / TempoControl and their implementation, in their OWN
// jar. Base = CLDC + MIDP merged; the surface is checked against cldc-1.1 + midp-2.0 + jsr-135-extra (signatures taken from the compiled
// JL-Mod interfaces, Apache-2.0, see docs/midp.md section 10).
val jsr135RomJar = layout.buildDirectory.file("rom/rom-jsr135.jar")
val midpBaseJar = tasks.register<Zip>("midpBaseJar") {
    description = "cldc + midp ROM merged (build input of the JSR-135 layer)"
    dependsOn(buildMidpRom)
    from(zipTree(romJar))
    from(zipTree(midpRomJar))
    destinationDirectory.set(layout.buildDirectory.dir("rom"))
    archiveFileName.set("base-midp.jar")
}
val buildJsr135Rom = tasks.register<JavaExec>("buildJsr135Rom") {
    group = "build"
    description = "Compiles vm-classlib/jsr135/src on top of the CLDC + MIDP ROM into rom-jsr135.jar (T27)"
    dependsOn(midpBaseJar)
    val srcDir = layout.projectDirectory.dir("jsr135/src")
    val dbDir = rootProject.layout.projectDirectory.dir("tools/api-sigs/db")
    inputs.dir(srcDir)
    inputs.file(midpBaseJar.flatMap { it.archiveFile })
    inputs.dir(dbDir)
    outputs.file(jsr135RomJar)
    classpath = romgen
    mainClass.set("jlkvm.tools.romgen.RomGen")
    args("--src", srcDir.asFile.path, "--base", layout.buildDirectory.file("rom/base-midp.jar").get().asFile.path, "--delta", "--out", jsr135RomJar.get().asFile.path,
        "--api-db", dbDir.file("cldc-1.1.json").asFile.path, "--api-db", dbDir.file("midp-2.0.json").asFile.path,
        "--api-db", dbDir.file("jsr-135-extra.json").asFile.path, "--api-ignore", "jlkvm/internal/")
}
tasks.named("assemble") { dependsOn(buildJsr135Rom) }
tasks.named("check") { dependsOn(buildJsr135Rom) }
