import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Locale
import java.util.Properties
import java.util.jar.Attributes
import java.util.jar.Manifest

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val secret = Properties().also { properties ->
    rootProject.file("keystore.properties").runCatching { inputStream().use(properties::load) }
}

android {
    compileSdk = rootProject.extra["compileSdk"] as Int
    ndkVersion = rootProject.extra["ndkVersion"] as String
    namespace = "ru.playsoftware.j2meloader"

    defaultConfig {
        applicationId = "ru.woesss.j2meloader"
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = 48
        versionName = "0.87.1"
        resValue("string", "app_name", rootProject.name)
        resValue("string", "app_center", secret.getProperty("appCenterKey", ""))
        resValue("string", "fingerprint", secret.getProperty("fingerprint", ""))
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    @Suppress("UnstableApiUsage")
    androidResources.generateLocaleConfig = true

    buildFeatures {
        viewBinding = true
        prefab = true
        buildConfig = true
    }

    signingConfigs.create("emulator") {
        if (secret.isNotEmpty()) {
            keyAlias = secret.getProperty("keyAlias")
            keyPassword = secret.getProperty("keyPassword")
            storeFile = rootProject.file(secret.getProperty("storeFile"))
            storePassword = secret.getProperty("storePassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        debug {
            applicationIdSuffix = ".debug"
            isJniDebuggable = true
            multiDexEnabled = true
            multiDexKeepProguard = file("multidex-config.pro")
        }
    }

    lint.disable += "MissingTranslation"

    testOptions.unitTests {
        isReturnDefaultValues = true
        all {
            it.systemProperty("jlkvm.root", rootProject.layout.projectDirectory.asFile.path)
        }
    }

    flavorDimensions += "default"
    productFlavors {
        create("emulator") { // variant dimension for create emulator
            buildConfigField("boolean", "FULL_EMULATOR", "true")
            signingConfig = signingConfigs.getByName("emulator")
            versionNameSuffix = System.getenv("VERSION_SUFFIX")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("midlet") { // variant dimension for create android port from J2ME app source
            buildConfigField("boolean", "FULL_EMULATOR", "false")
            // configure midlet's port project params here, as default it read from app manifest,
            // placed to 'app/src/midlet/resources/MIDLET-META-INF/MANIFEST.MF'
            val props = getMidletManifestProperties()
            val midletName = props.getValue("MIDlet-Name")?.trim() ?: "Demo MIDlet"
            val apkName = midletName.replace("[/\\\\:*?\"<>|]".toRegex(), "").replace(" ", "_")
            applicationId = "com.example.androidlet.${apkName.lowercase(Locale.getDefault())}"
            versionName = props.getValue("MIDlet-Version") ?: "1.0"
            resValue("string", "app_name", midletName)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-midlet.pro"
            )
        }
    }

    splits.abi {
        isEnable = true
        reset()
        include("x86", "armeabi-v7a", "x86_64", "arm64-v8a")
        isUniversalApk = true
    }

    externalNativeBuild.ndkBuild.path("src/main/cpp/Android.mk")

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    applicationVariants.configureEach {
        if (buildType.name == "debug" && flavorName == "emulator") {
            resValue("string", "app_name", "JL-Debug")
        }
        outputs.configureEach {
            if (this is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                outputFileName = "${rootProject.name}_$versionName-$dirName.apk"
            }
        }
    }
}

kotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_17)

fun getMidletManifestProperties(): Attributes = Manifest().let { mf ->
    project.file("src/midlet/resources/MIDLET-META-INF/MANIFEST.MF").runCatching {
        inputStream().use(mf::read)
    }
    return mf.mainAttributes
}

// PLAN T34/T35: assets built from the rest of the repository: the jarscan databases + profile (install-time scan) and the guest ROM layers
// (vm-classlib) the VM loads; generated, never committed
val jlkvmAssets = layout.buildDirectory.dir("generated/jlkvm-assets")
val copyJarscanAssets by tasks.registering(Copy::class) {
    from(rootProject.file("tools/api-sigs/db")) {
        include("cldc-1.1.json", "midp-2.0.json", "s40dp2-nokia-ui.json", "s40dp2-wma-1.1.json", "jsr75-fc.json", "jsr75-pim.json")
        into("jarscan/db")
    }
    from(rootProject.file("tools/jarscan/profiles/cldc11-midp20-nokiaui-jsr.json")) {
        rename { "profile.json" }
        into("jarscan")
    }
    from(rootProject.file("profiles/jlkvm-nokia.json")) { into("jlkvm") }
    into(jlkvmAssets)
}
val copyRomAssets by tasks.registering(Copy::class) {
    dependsOn(":vm-classlib:buildRom", ":vm-classlib:buildMidpRom", ":vm-classlib:buildJsr135Rom", ":vm-classlib:buildNokiaUiRom",
        ":vm-classlib:buildWmaRom", ":vm-classlib:buildJsr75FcRom", ":vm-classlib:buildJsr75PimRom")
    from(rootProject.file("vm-classlib/build/rom")) { include("rom*.jar"); into("jlkvm/rom") }
    into(jlkvmAssets)
}
android.sourceSets.getByName("main").assets.srcDir(jlkvmAssets)
tasks.matching { it.name.startsWith("merge") && it.name.endsWith("Assets") }.configureEach {
    dependsOn(copyJarscanAssets, copyRomAssets)
}

dependencies {
    // D17: the dex converter exists only in the debug build (the old engine, kept for A/B against the VM); release has no dex code at all
    debugImplementation(projects.dexlib)
    implementation(projects.hostAndroid)
    implementation(projects.tools.jarscan)
    implementation(projects.tools.apiSigs)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.arch.core.common)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.concurrent.futures)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.recyclerview)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.transition)

    annotationProcessor(libs.google.auto.service)
    compileOnly(libs.google.auto.service.annotations)
    implementation(libs.google.gson)
    implementation(libs.google.material)
    implementation(libs.google.oboe)

    implementation(libs.acra.http)
    implementation(libs.ambilwarna)
    implementation(libs.donations)
    implementation(libs.ffmpeg.mobile)
    implementation(libs.filepicker)
    implementation(libs.pngj)
    implementation(libs.rx.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
