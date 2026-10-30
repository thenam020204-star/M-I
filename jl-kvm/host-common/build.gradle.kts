plugins {
    `java-library`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)   // shared by host-desktop and host-android (ART)
    options.encoding = "UTF-8"
}

dependencies {
    api(project(":vm-core"))
    testImplementation(libs.junit)
}

// Portable host code (JAD/manifest, AMS natives, software raster, PNG decoder, lcdui natives): pure Java, no android.* (PLAN D2 applies to every shared module).
val checkNoAndroid = tasks.register("checkNoAndroidImports") {
    group = "verification"
    description = "Fails if host-common sources reference android.*"
    val srcs = fileTree("src") { include("**/*.java") }
    inputs.files(srcs)
    doLast {
        val re = Regex("""(^|[^\w.])android\.[a-z]""")
        val bad = mutableListOf<String>()
        srcs.forEach { f -> f.readLines().forEachIndexed { i, l -> if (re.containsMatchIn(l)) bad += "${f.relativeTo(projectDir)}:${i + 1}: ${l.trim()}" } }
        if (bad.isNotEmpty()) throw GradleException("host-common must not reference android.*:\n" + bad.joinToString("\n"))
    }
}
tasks.named("compileJava") { dependsOn(checkNoAndroid) }

// The tone sequence compiler and the RTTTL parser are guest code (-source 1.3, java.lang only) shared with the unit tests: compiled here too.
sourceSets.test {
    java.srcDir("../vm-classlib/midp/src")
    java.srcDir("../vm-classlib/nokiaui/src")
    java.include("jlkvm/host/**", "jlkvm/internal/media/tone/**", "jlkvm/internal/nokia/Ott.java")
}

tasks.test {
    useJUnit()
}
