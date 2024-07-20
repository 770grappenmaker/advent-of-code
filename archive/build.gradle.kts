plugins {
    id("kotlin")
    id("ksp")
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.util)
    implementation(libs.serialization.json)
}
