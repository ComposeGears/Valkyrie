import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformDependenciesExtension
import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform
import org.jetbrains.intellij.platform.gradle.plugins.project.IntelliJPlatformBasePlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.changelog) apply false
    alias(libs.plugins.jetbrains.intellij) apply false
    alias(libs.plugins.jetbrains.intellij.module) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    plugins.withType<IntelliJPlatformBasePlugin>().configureEach {
        // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html#configuration.repositories
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

            intellijPlatform {
                defaultRepositories()
            }
        }
        // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html#setting-up-intellij-platform
        dependencies {
            extensions.configure<IntelliJPlatformDependenciesExtension> {
                // https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html
                intellijIdea("2025.2.4")

                bundledPlugin("org.jetbrains.kotlin")

                // https://github.com/JetBrains/intellij-platform-compose-plugin-template
                composeUI()
            }
        }
    }

    plugins.apply(rootProject.libs.plugins.spotless.get().pluginId)
    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("src/test/resources/**")
            ktlint(libs.ktlint.get().version)
                .editorConfigOverride(
                    mapOf(
                        "ktlint_compose_lambda-param-event-trailing" to "disabled",
                        "compose_treat_as_lambda" to false,
                        "compose_disallow_material2" to true,
                        "compose_allowed_from_m2" to "icons",
                        "compose_preview_naming_enabled" to true,
                        "compose_preview_naming_strategy" to "suffix",
                    ),
                )
                .customRuleSets(
                    listOf(
                        libs.composeRules.get().toString(),
                    ),
                )
        }
        kotlinGradle {
            ktlint(libs.ktlint.get().version)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release = libs.versions.jdkRelease.get().toInt()
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(libs.versions.jdkRelease.get())
            freeCompilerArgs.add("-Xjdk-release=${libs.versions.jdkRelease.get()}")
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
