import java.net.URLClassLoader
import java.time.OffsetDateTime
import java.time.ZoneOffset

plugins {
    id("kotlin")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":util"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}