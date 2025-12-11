plugins {
    id("kotlin")
//    id("ksp")
}

dependencies {
    implementation(projects.util)
//    implementation(rootProject.layout.files("kotlin-z3-bindings.jar"))
//    implementation(rootProject.layout.files("z3kt-jvm-0.6.0.jar"))
//    implementation(rootProject.layout.files("com.microsoft.z3.jar"))
//    implementation(libs.bundles.asm)
}