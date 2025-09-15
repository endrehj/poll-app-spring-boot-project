pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "demo"

include("poll-app")
project(":poll-app").projectDir = file("poll-app")