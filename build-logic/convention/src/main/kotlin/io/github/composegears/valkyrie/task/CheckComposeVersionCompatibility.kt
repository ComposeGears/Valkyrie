package io.github.composegears.valkyrie.task

import net.swiftzer.semver.SemVer
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
        val maxSupportedVersion = SemVer.parse(expectedComposeVersion.get())

        val composeDependencies = artifactCollection.get().artifacts
            .mapNotNull { it.id.componentIdentifier as? ModuleComponentIdentifier }
            .filter { it.group.startsWith("org.jetbrains.compose") }

        val invalidVersions = composeDependencies.filter {
            SemVer.parse(it.version) > maxSupportedVersion
        }

        if (invalidVersions.isNotEmpty()) {
            val errorMessage = buildString {
                appendLine("Found Compose dependencies with version > $maxSupportedVersion:")
                invalidVersions.forEach {
                    appendLine("\t- ${it.group}:${it.module}:${it.version}")
                }
            }
            throw GradleException(errorMessage)
        }
        logger.lifecycle("✅ All compose dependencies have version $maxSupportedVersion or lower")
    }
}
