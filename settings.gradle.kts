rootProject.name = "aoc"
includeBuild("nasty-jvm-util") {
    dependencySubstitution {
        substitute(module("com.grappenmaker:nasty-jvm-util")).using(project(":"))
    }
}

//include("archive", "visualisations")
include("archive", "current", "runner", "util")

pluginManagement {
    plugins {
        kotlin("plugin.serialization") version "2.0.0-Beta2"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}