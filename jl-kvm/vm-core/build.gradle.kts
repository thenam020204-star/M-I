plugins {
    `java-library`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)   // runs on Android (ART) and desktop JVM
    options.encoding = "UTF-8"
}

dependencies {
    testImplementation(libs.junit)
}

// D2: vm-core is pure Java. Fail the build if any source references the Android SDK.
val checkNoAndroid = tasks.register("checkNoAndroidImports") {
    group = "verification"
    description = "Fails if vm-core sources reference android.* (PLAN D2)"
    val srcs = fileTree("src") { include("**/*.java", "**/*.kt") }
    inputs.files(srcs)
    doLast {
        val bad = mutableListOf<String>()
        val re = Regex("""(^|[^\w.])android\.[a-z]""")
        srcs.forEach { f ->
            f.readLines().forEachIndexed { i, line ->
                val t = line.trim()
                if (t.startsWith("import android.") || t.startsWith("import static android.") || re.containsMatchIn(line)) {
                    bad += "${f.relativeTo(projectDir)}:${i + 1}: $t"
                }
            }
        }
        if (bad.isNotEmpty()) {
            throw GradleException("vm-core must not reference android.* (D2):\n" + bad.joinToString("\n"))
        }
    }
}
tasks.named("compileJava") { dependsOn(checkNoAndroid) }
tasks.named("check") { dependsOn(checkNoAndroid) }

tasks.test {
    useJUnit()
}
