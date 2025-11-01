package io.github.composegears.valkyrie.task

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.artifacts.ArtifactCollection
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class CheckComposeVersionCompatibility : DefaultTask() {
    @get:Internal
    abstract val artifactCollection: Property<ArtifactCollection>

    @get:Input
    abstract val expectedComposeVersion: Property<String>

    @TaskAction
    fun checkVersions() {
        val expectedVersion = expectedComposeVersion.get()

        val composeDependencies = artifactCollection.get().artifacts
            .mapNotNull { it.id.componentIdentifier as? ModuleComponentIdentifier }
            .filter { it.group.startsWith("org.jetbrains.compose") }

        val invalidVersions = composeDependencies.filter {
            compareVersions(currentVersion = it.version, expectedVersion = expectedVersion) > 0
        }

        if (invalidVersions.isNotEmpty()) {
            val errorMessage = buildString {
                appendLine("Found org.jetbrains.compose dependencies with version > $expectedVersion:")
                invalidVersions.forEach {
                    appendLine("  - ${it.group}:${it.module}:${it.version}")
                }
            }
            throw GradleException(errorMessage)
        }
        logger.lifecycle("✅ All compose dependencies have version $expectedVersion or lower")
    }

    private fun compareVersions(currentVersion: String, expectedVersion: String): Int {
        val current = parseVersion(currentVersion)
        val expected = parseVersion(expectedVersion)

        return when {
            current.major != expected.major -> current.major.compareTo(expected.major)
            current.minor != expected.minor -> current.minor.compareTo(expected.minor)
            current.patch != expected.patch -> current.patch.compareTo(expected.patch)
            else -> 0
        }
    }

    private fun parseVersion(version: String): Version {
        val parts = version.split('.', '-').mapNotNull { it.toIntOrNull() }
        return Version(
            major = parts[0],
            minor = parts[1],
            patch = parts[2],
        )
    }
    private data class Version(
        val major: Int,
        val minor: Int,
        val patch: Int,
    )
}
