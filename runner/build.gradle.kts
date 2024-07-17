plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":archive"))
    implementation(project(":current"))
    implementation(project(":util"))
}

tasks {
    val jarProvider = named<Jar>("jar") {
        from(configurations.runtimeClasspath.map { conf ->
            conf.map { if (it.isDirectory) it else zipTree(it) }.toTypedArray()
        })

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    register<JavaExec>("solution") {
        dependsOn(jarProvider)
        dependsOn(build)

        mainClass.set("com.grappenmaker.aoc.SolutionRunner")
        classpath(jarProvider)
        workingDir(rootProject.projectDir)

        // Huge gradle error message isn't fun
        isIgnoreExitValue = true
    }
}