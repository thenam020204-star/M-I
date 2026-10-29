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
    implementation(libs.asm)
    implementation(libs.google.gson)
    testImplementation(libs.junit)
}

tasks.test {
    useJUnit()
}

dependencies {
    implementation(project(":tools:api-sigs"))
}

application {
    mainClass.set("jlkvm.tools.jarscan.Main")
}
