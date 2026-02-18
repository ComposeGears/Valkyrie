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
    id("com.gradle.develocity") version "4.3.2"
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
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://www.jetbrains.com/intellij-repository/snapshots")
        maven("https://cache-redirector.jetbrains.com/intellij-dependencies")

        versionCatalogs {
            create("cli") {
                from(files("gradle/cli.versions.toml"))
            }
            create("gradlePluginVersions") {
                from(files("gradle/gradle.versions.toml"))
            }
            create("ideaPluginVersions") {
                from(files("gradle/plugin.versions.toml"))
            }
        }
    }
}

if (JavaVersion.current() < JavaVersion.VERSION_21) {
    throw GradleException("Java 21 or higher is required to run this project. Current version: ${JavaVersion.current()}")
}

rootProject.name = "valkyrie"

includeBuild("build-logic")

include("tools:cli")
include("tools:compose-app")
include("tools:gradle-plugin")
include("tools:idea-plugin")

include("components:generator:core")
include("components:generator:iconpack")
include("components:generator:jvm:poet-extensions")
include("components:generator:jvm:imagevector")
include("components:parser:common")
include("components:parser:jvm:svg")
include("components:parser:jvm:xml")
include("components:parser:kmp:xml")
include("components:parser:kmp:svg")
include("components:parser:unified")

include("sdk:compose:codeviewer")
include("sdk:compose:highlights-core")
include("sdk:compose:icons")
include("sdk:compose:foundation")
include("sdk:core:extensions")
include("sdk:core:xml")
include("sdk:generator:xml")
include("sdk:intellij:psi:iconpack")
include("sdk:intellij:psi:imagevector")
include("sdk:intellij:test-fixtures")
include("sdk:ir:compose")
include("sdk:ir:core")
include("sdk:ir:util")
include("sdk:ir:xml")
include("sdk:shared")
include("sdk:test:coverage")
include("sdk:test:resource-loader")
