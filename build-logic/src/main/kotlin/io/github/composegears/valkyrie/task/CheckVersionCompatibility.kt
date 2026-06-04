package io.github.composegears.valkyrie.task

import net.swiftzer.semver.SemVer
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class CheckVersionCompatibility : DefaultTask() {

    /** Maps each resolved coordinate (`group:name:version`) to the list of coordinates that depend on it. */
    @get:Input
    abstract val resolvedComponents: MapProperty<String, List<String>>

    @get:Input
    abstract val maxKotlinVersion: Property<String>

    @get:Input
    abstract val maxComposeVersion: Property<String>

    @get:Input
    abstract val maxCoroutinesVersion: Property<String>

    @TaskAction
    fun check() {
        val maxKotlin = SemVer.parse(maxKotlinVersion.get())
        val maxCompose = SemVer.parse(maxComposeVersion.get())
        val maxCoroutines = SemVer.parse(maxCoroutinesVersion.get())

        val violations = resolvedComponents.get()
            .mapNotNull { (coordinate, dependents) ->
                val (group, name, version) = coordinate.split(":")
                val requiredBy = if (dependents.isNotEmpty()) {
                    "\n" + dependents.joinToString("\n") { "\t\t  - $it" }
                } else {
                    ""
                }
                when {
                    group == "org.jetbrains.kotlin" && SemVer.parse(version) > maxKotlin ->
                        "\t- $group:$name:$version  (max supported: $maxKotlin)$requiredBy"
                    group.startsWith("org.jetbrains.compose") && SemVer.parse(version) > maxCompose ->
                        "\t- $group:$name:$version  (max supported: $maxCompose)$requiredBy"
                    group == "org.jetbrains.kotlinx" && name.startsWith("kotlinx-coroutines") && SemVer.parse(version) > maxCoroutines ->
                        "\t- $group:$name:$version  (max supported: $maxCoroutines)$requiredBy"
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
        logger.lifecycle("✅ All dependencies are compatible (kotlin ≤ $maxKotlin, compose ≤ $maxCompose, coroutines ≤ $maxCoroutines)")
    }
}
