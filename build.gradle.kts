import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.intellij) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    group = rootProject.providers.gradleProperty("GROUP").get()
    version = rootProject.providers.gradleProperty("VERSION_NAME").get()

    plugins.apply(rootProject.libs.plugins.spotless.get().pluginId)
    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("src/test/resources/**")
            ktlint(rootProject.libs.ktlint.get().version)
                .customRuleSets(
                    listOf(
                        rootProject.libs.composeRules.get().toString(),
                    ),
                )
        }
        kotlinGradle {
            ktlint(rootProject.libs.ktlint.get().version)
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        // https://docs.gradle.org/8.9/userguide/performance.html#execute_tests_in_parallel
        maxParallelForks = Runtime.getRuntime().availableProcessors()
    }
}
