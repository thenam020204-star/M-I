plugins {
    application
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
    options.encoding = "UTF-8"
}

dependencies {
    implementation(project(":vm-core"))
    implementation(project(":tools:api-sigs"))
    implementation(libs.asm)
    implementation(libs.google.gson)
    // D11: ProGuard -microedition is the preverifier (PLAN V1; WTK `preverify` is not used)
    implementation("com.guardsquare:proguard-base:7.5.0")
    testImplementation(libs.junit)
}

tasks.test {
    useJUnit()
}

application {
    mainClass.set("jlkvm.tools.romgen.RomGen")
}
