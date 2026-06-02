package io.github.composegears.valkyrie.task

import net.swiftzer.semver.SemVer
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class CheckVersionCompatibility : DefaultTask() {

    @get:Input
    abstract val resolvedComponents: ListProperty<String>

    @get:Input
    abstract val maxKotlinVersion: Property<String>

    @get:Input
    abstract val maxComposeVersion: Property<String>

    @TaskAction
    fun check() {
        val maxKotlin = SemVer.parse(maxKotlinVersion.get())
        val maxCompose = SemVer.parse(maxComposeVersion.get())

        val violations = resolvedComponents.get()
            .mapNotNull { coordinate ->
                val (group, name, version) = coordinate.split(":")
                when {
                    group == "org.jetbrains.kotlin" && SemVer.parse(version) > maxKotlin ->
                        "\t- $group:$name:$version  (max supported: $maxKotlin)"
                    group.startsWith("org.jetbrains.compose") && SemVer.parse(version) > maxCompose ->
                        "\t- $group:$name:$version  (max supported: $maxCompose)"
                    else -> null
                }
            }
            .distinct()

        if (violations.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Incompatible dependency versions found:")
                    violations.forEach(::appendLine)
                },
            )
        }
        logger.lifecycle("✅ All dependencies are compatible (kotlin ≤ $maxKotlin, compose ≤ $maxCompose)")
    }
}
