plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.grappenmaker:nasty-jvm-util") {
        capabilities {
            requireCapability("com.grappenmaker:nasty-jvm-util-reflect")
        }
    }
}

kotlin { jvmToolchain(20) }