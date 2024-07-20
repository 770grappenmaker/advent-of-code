plugins {
    id("com.google.devtools.ksp")
    id("kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ksp"))
    ksp(project(":ksp"))
}