plugins {
    id("kotlin")
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    api("com.grappenmaker:nasty-jvm-util") {
        capabilities {
            requireCapability("com.grappenmaker:nasty-jvm-util-reflect")
        }
    }
}