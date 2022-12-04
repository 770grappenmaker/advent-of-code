import java.net.URLClassLoader
import java.time.*

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

    register("today") {
        // This one is a little faster because it doesn't spawn a new java process
        // This is an optimization, but it can be dangerous since, well, it runs in the same process.
        // If the program decides to quit because input is missing for example, the gradle daemon gets killed.
        // Only use when testing today's problem for optimization purposes!
        dependsOn("classes")

        doLast {
            val unlockOffset = ZoneOffset.ofHours(-5)
            val date = OffsetDateTime.now().withOffsetSameInstant(unlockOffset)
            val year = date.year
            val day = date.dayOfMonth
            val inputFile = file("inputs/$year/day-${"%02d".format(day)}.txt")
                .also { if (!it.exists()) throw GradleException("Input file $it is not available!") }
                .absolutePath

            val args = arrayOf(day.toString(), year.toString(), inputFile)

            val classpath = sourceSets.main.get().output.classesDirs + configurations.runtimeClasspath.get().files
            val loader = URLClassLoader((classpath.map { it.toURI().toURL() }).toTypedArray())

            runCatching {
                Class.forName("com.grappenmaker.aoc.SolutionRunner", true, loader)
                    .getMethod("main", args::class.java)(null, args)
            }.onFailure {
                logger.error("Failed to run solution: $it")
                it.printStackTrace()
            }
        }
    }
}