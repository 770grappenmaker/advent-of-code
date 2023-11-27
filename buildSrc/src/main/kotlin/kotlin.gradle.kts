plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(20)

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}