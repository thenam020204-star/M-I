plugins {
    id("com.android.library")
}

android {
    compileSdk = rootProject.extra["compileSdk"] as Int
    namespace = "jlkvm.host.android"

    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api(project(":vm-core"))
    api(project(":host-common"))
    api(project(":host-desktop"))   // DesktopHost / RomLayers / JarClassSource / VmRunner are plain Java (no AWT): the Android host reuses them
}
