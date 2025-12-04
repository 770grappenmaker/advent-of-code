plugins {
    id("kotlin")
}

dependencies {
    implementation(projects.util)

    // TODO: convert to version catalog
    val gdxVersion = "1.11.0"
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    ktxImplementation("app", "assets", "collections", "graphics")
}

fun DependencyHandlerScope.ktxImplementation(vararg names: String) =
    names.forEach { implementation("io.github.libktx:ktx-$it:1.11.0-rc3") }