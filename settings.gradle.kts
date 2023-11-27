rootProject.name = "aoc"
includeBuild("nasty-jvm-util") {
    dependencySubstitution {
        substitute(module("com.grappenmaker:nasty-jvm-util")).using(project(":"))
    }
}

//include("archive", "visualisations")
include("current", "runner", "util")

pluginManagement {
    plugins {
        kotlin("plugin.serialization") version "1.9.20"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}