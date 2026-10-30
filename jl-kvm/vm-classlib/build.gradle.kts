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
