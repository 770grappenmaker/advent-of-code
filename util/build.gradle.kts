plugins {
    id("kotlin")
}

dependencies {
    api(libs.coroutines)
}

tasks {
    val inputJar by registering(Jar::class) {
        dependsOn(classes)
        from(sourceSets.main.map { it.output.classesDirs })
        from(configurations.runtimeClasspath.map { conf ->
            conf.map { if (it.isDirectory) it else zipTree(it) }
        })

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier = "input"
    }

    assemble { dependsOn(inputJar) }
}