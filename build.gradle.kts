import com.diffplug.gradle.spotless.SpotlessExtension
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
    alias(libs.plugins.spotless) apply false
}

allprojects {
    group = rootProject.providers.gradleProperty("GROUP").get()
    version = rootProject.providers.gradleProperty("VERSION_NAME").get()

    plugins.withType<KotlinBasePlugin>().configureEach {
        dependencies {
            // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
            "compileOnly"(libs.kotlin.stdlib)
        }
    }

    plugins.withId(rootProject.libs.plugins.kotlin.compose.get().pluginId) {
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            enableStrongSkippingMode = true
            stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
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
    }

    plugins.apply(rootProject.libs.plugins.spotless.get().pluginId)
    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("src/test/resources/**")
            ktlint(libs.ktlint.get().version)
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
