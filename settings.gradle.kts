enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("current", "runner", "util")

pluginManagement {
    plugins {
        kotlin("plugin.serialization") version "2.0.21"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "advent-of-code"