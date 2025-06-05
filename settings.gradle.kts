enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version "4.0.2"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        // TODO: workaround for https://github.com/gradle/gradle/issues/22879.
        val isCI = providers.environmentVariable("CI").isPresent
        publishing.onlyIf { isCI }
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

includeBuild("build-logic")

include("tools:cli")
include("tools:compose-app")
include("tools:idea-plugin")

include("components:extensions")
include("components:generator:core")
include("components:generator:iconpack")
include("components:generator:jvm:poet-extensions")
include("components:generator:jvm:imagevector")
include("components:ir")
include("components:ir-compose")
include("components:parser:common")
include("components:parser:jvm:svg")
include("components:parser:jvm:xml")
include("components:parser:kmp:xml")
include("components:parser:kmp:svg")
include("components:parser:unified")
include("components:psi:iconpack")
include("components:psi:imagevector")
include("components:test:coverage")

include("compose:core")
include("compose:icons")
include("compose:ui")

include("shared")
