/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("buildlogic.java-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation(project(":core"))
}

application {
    // Define the main class for the application.
    mainClass = "org.example.app.Desktop"
    applicationDefaultJvmArgs = mutableListOf("-XstartOnFirstThread")
}