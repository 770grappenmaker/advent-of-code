enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("current", "util")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "advent-of-code"