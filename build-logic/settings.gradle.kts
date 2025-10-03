dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("dev.panuszewski.typesafe-conventions") version "0.8.1"
}

rootProject.name = "build-logic"
include(":convention")
