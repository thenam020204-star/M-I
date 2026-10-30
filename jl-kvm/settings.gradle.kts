enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "JL-Mod"
include(":app", ":dexlib")
// jl-kvm modules (see PLAN.md)
include(
    ":vm-core", ":vm-classlib", ":host-common", ":host-android", ":host-desktop",
    ":tools:api-sigs", ":tools:jarscan", ":tools:romgen", ":tools:difftest",
)
