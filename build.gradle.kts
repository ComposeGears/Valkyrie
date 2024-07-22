import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.intellij) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    plugins.apply(rootProject.libs.plugins.spotless.get().pluginId)
    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            // Sources copied from AndroidX Compose
            targetExclude("src/main/kotlin/androidx/**")
            ktlint(rootProject.libs.ktlint.get().version)
        }
        kotlinGradle {
            ktlint(rootProject.libs.ktlint.get().version)
        }
    }
}
