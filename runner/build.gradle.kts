plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation(project(":archive"))
    implementation(project(":current"))
    implementation(project(":util"))
}

kotlin { jvmToolchain(20) }