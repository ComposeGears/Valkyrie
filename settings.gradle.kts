enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "valkyrie"

include("plugin")

include("components:google")
include("components:generator:common")
include("components:generator:iconpack")
include("components:generator:imagevector")
include("components:parser")