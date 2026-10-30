plugins {
    `java-library`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
    options.encoding = "UTF-8"
}

dependencies {
    api(project(":vm-core"))
    api(project(":host-common"))
    testImplementation(libs.junit)
}

// the tests run the guest on the real ROM layers and on MIDlet jars built by tests/midlets/build.sh
tasks.test {
    useJUnit()
    dependsOn(":vm-classlib:buildMidpRom")
    systemProperty("jlkvm.rom.dir", rootProject.layout.projectDirectory.dir("vm-classlib/build/rom").asFile.path)
    systemProperty("jlkvm.root", rootProject.layout.projectDirectory.asFile.path)
    jvmArgs("-Xss4m")
}
