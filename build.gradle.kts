import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.gradle.dependency.check)
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.intellij) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
        version.uppercase().contains(it)
    }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}