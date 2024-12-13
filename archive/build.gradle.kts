plugins {
    id("kotlin")
    id("ksp")
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.util)
    implementation(libs.serialization.json)
    implementation(rootProject.files("kotlin-z3-bindings.jar"))
}
