plugins {
    id("kotlin")
//    id("ksp")
}

dependencies {
    implementation(projects.util)
    implementation(rootProject.files("kotlin-z3-bindings.jar"))
}