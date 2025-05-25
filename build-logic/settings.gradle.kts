dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("dev.panuszewski.typesafe-conventions") version "0.7.2"
}

rootProject.name = "build-logic"
include(":convention")
