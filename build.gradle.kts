import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformDependenciesExtension
import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform
import org.jetbrains.intellij.platform.gradle.plugins.project.IntelliJPlatformBasePlugin
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.intellij) apply false
    alias(libs.plugins.jetbrains.intellij.module) apply false
    alias(libs.plugins.buildConfig) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    plugins.withType<KotlinBasePlugin>().configureEach {
        dependencies {
            // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
            "compileOnly"(libs.kotlin.stdlib)
        }
    }

    plugins.withId(rootProject.libs.plugins.kotlin.compose.get().pluginId) {
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFiles.addAll(rootProject.layout.projectDirectory.file("stability_config.conf"))
        }
    }

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
            intellijPlatform {
                defaultRepositories()
            }
        }
        // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html#setting-up-intellij-platform
        dependencies {
            extensions.configure<IntelliJPlatformDependenciesExtension> {
                intellijIdeaCommunity(libs.versions.idea)
                instrumentationTools()
                // dependency plugin id for https://plugins.jetbrains.com/plugin/6954-kotlin
                bundledPlugin(libs.kotlin.stdlib.map(Dependency::getGroup))
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

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        // https://docs.gradle.org/8.9/userguide/performance.html#execute_tests_in_parallel
        maxParallelForks = Runtime.getRuntime().availableProcessors()
    }

    configurations.configureEach {
        resolutionStrategy.eachDependency {
            if (requested.group == libs.kotlin.stdlib.get().group) {
                useVersion(libs.versions.kotlin.get())
            }
        }
    }
}
