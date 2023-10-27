plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":util"))
}

kotlin { jvmToolchain(20) }