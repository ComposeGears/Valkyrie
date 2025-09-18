dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("dev.panuszewski.typesafe-conventions") version "0.8.0"
}

rootProject.name = "build-logic"
include(":convention")
