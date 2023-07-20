rootProject.name = "aoc"
includeBuild("nasty-jvm-util") {
    dependencySubstitution {
        substitute(module("com.grappenmaker:nasty-jvm-util")).using(project(":"))
    }
}

include("visualisations")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.0"
        kotlin("plugin.serialization") version "1.9.0"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}