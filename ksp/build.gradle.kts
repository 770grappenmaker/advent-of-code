plugins {
    id("kotlin")
}

kotlin { jvmToolchain(8) }

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.poet)
    implementation(libs.poet.ksp)
}