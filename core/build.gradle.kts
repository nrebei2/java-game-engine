import java.util.*

plugins {
    id("buildlogic.java-library-conventions")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    val lwjglVersion = "3.3.1"
    val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
    val osArch = System.getProperty("os.arch").lowercase(Locale.getDefault())

    val lwjglNatives = when {
        osName.contains("win") && osArch.contains("64") -> "natives-windows"
        osName.contains("mac") && osArch.contains("x86_64") -> "natives-macos"
        osName.contains("mac") && osArch.contains("aarch64") -> "natives-macos-arm64"
        osName.contains("nix") || osName.contains("nux") || osName.contains("aix") -> "natives-linux"
        else -> throw GradleException("Unsupported OS or architecture: $osName, $osArch")
    }

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-assimp")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
}