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
                // https://youtrack.jetbrains.com/articles/IDEA-A-21/IDEA-Latest-Builds-And-Release-Notes
                intellijIdea("2026.2.3")

                bundledPlugin("org.jetbrains.kotlin")
                bundledPlugin("com.intellij.java.ide")

                // https://github.com/JetBrains/intellij-platform-compose-plugin-template
                composeUI()
            }
        }

        // IntelliJ IDEA IU 2026.2.3 registers `<postStartupActivity implementation="Z.Z.Z.Z.Z"/>` from
        // `plugins/ultimate-plugin/lib/ultimate-plugin.jar`, but that class name also exists in the core
        // `lib/product-backend.jar` as an interface. Unit tests run with `-Didea.force.use.core.classloader=true`,
        // so the core interface wins and creating the activity fails with "Cannot find suitable constructor",
        // breaking every platform test. The Ultimate plugin is unused in tests, so disable it in the test config.
        tasks.withType<Test>().configureEach {
            doFirst {
                val configPath = allJvmArgs.firstNotNullOfOrNull { arg ->
                    "-Didea.config.path=".takeIf { arg.startsWith(it) }?.let { arg.removePrefix(it) }
                }
                if (configPath != null) {
                    val disabled = File(configPath, "disabled_plugins.txt")
                    val marker = "com.intellij.modules.ultimate"
                    val current = disabled.takeIf { it.isFile }?.readLines().orEmpty()
                    if (marker !in current) {
                        disabled.writeText((current + marker).joinToString("\n", postfix = "\n"))
                    }
                }
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
        useJUnitPlatform {
            excludeEngines("junit-vintage")
        }
    }
}
