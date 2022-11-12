plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

group = "com.grappenmaker"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    val asmVersion = "9.4"
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("org.ow2.asm:asm-commons:$asmVersion")
    implementation("org.ow2.asm:asm-util:$asmVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

tasks {
    withType<Jar>().configureEach {
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    register<JavaExec>("solution") {
        val jarTask = jar.get()
        dependsOn(jarTask.path)

        mainClass.set("com.grappenmaker.aoc.SolutionRunner")
        classpath(*jarTask.outputs.files.files.toTypedArray())

        // Huge gradle error message isn't fun
        isIgnoreExitValue = true
    }
}