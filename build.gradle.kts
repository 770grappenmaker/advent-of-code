import java.lang.reflect.InvocationTargetException
import java.net.URLClassLoader

plugins {
    kotlin("jvm") version "1.6.0"
}

val mainClassPath by extra("com.grappenmaker.aoc2021.AOC2021")

group = "com.grappenmaker"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to mainClassPath)
    }

    from(getRuntimeClasspath().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register("runSolution") {
    dependsOn("classes")
    doLast {
        val paths = (getRuntimeClasspath().map { it.toURI().toURL() } +
                sourceSets.main.get().output.classesDirs.map { it.toURI().toURL() }).toTypedArray()
        val newLoader = URLClassLoader(paths)

        val parameters = arrayOf(properties["day"] as String? ?: "1")
        try {
            Class.forName(mainClassPath, true, newLoader).getMethod("main", parameters::class.java)(null, parameters)
        } catch (e: InvocationTargetException) {
            throw GradleScriptException("Exception while executing solution", e.targetException)
        }
    }
}

fun getRuntimeClasspath() = configurations.runtimeClasspath.get()