plugins {
    id("kotlin")
//    id("ksp")
}

dependencies {
//    implementation(projects.archive)
    implementation(projects.current)
    implementation(projects.util)
//    implementation(libs.bundles.asm)
}

//ksp {
//    arg("generatePuzzleSets", "false")
//    arg("generateYearSet", "true")
//}

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