import java.lang.reflect.InvocationTargetException
import java.net.URLClassLoader

plugins {
    kotlin("jvm") version "1.6.0"
}

val mainClassPath by extra("com.grappenmaker.aoc2021.AOC2021")
val benchmarkClass by extra("com.grappenmaker.aoc2021.Benchmark")

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
        runClass(mainClassPath, getParameters())
    }
}

tasks.register("benchmark") {
    dependsOn("classes")
    doLast {
        runClass(benchmarkClass, getParameters())
    }
}

fun runClass(name: String, parameters: Array<String>) {
    try {
        val newLoader = getLoader()
        Class.forName(name, true, newLoader)
            .getMethod("main", parameters::class.java)(null, parameters)
    } catch (e: InvocationTargetException) {
        throw GradleScriptException("Exception while benchmarking solution", e.targetException)
    }
}

fun getLoader(): ClassLoader {
    val paths = (getRuntimeClasspath().map { it.toURI().toURL() } +
            sourceSets.main.get().output.classesDirs.map { it.toURI().toURL() }).toTypedArray()

    return URLClassLoader(paths)
}

fun getRuntimeClasspath() = configurations.runtimeClasspath.get()
fun getParameters() = (properties["args"] as String? ?: "").split(" ").toTypedArray()