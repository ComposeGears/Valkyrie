package io.github.composegears.valkyrie.gradle

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

@Suppress("unused") // Registered as a Gradle plugin.
class ValkyrieGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val extension = extensions.create("valkyrie", ValkyrieExtension::class.java)

        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            registerTasks<KotlinJvmProjectExtension>(extension, isMultiplatform = false)
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            registerTasks<KotlinMultiplatformExtension>(extension, isMultiplatform = true)
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.android") {
            pluginManager.withPlugin("com.android.base") {
                registerTasks<KotlinAndroidProjectExtension>(extension, isMultiplatform = false)
            }
        }

        val codegenTasks = tasks.withType(GenerateSvgImageVectorTask::class.java)

        // Run generation immediately if we're syncing Intellij/Android Studio - helps to speed up dev cycle
        val isIdeSyncing = System.getProperty("idea.sync.active") == "true"
        if (extension.generateAtSync.getOrElse(false) && isIdeSyncing) {
            tasks.findByName("prepareKotlinIdeaImport")?.dependsOn(codegenTasks)
        }

        // Create a wrapper task to invoke all other codegen tasks
        tasks.register(GenerateSvgImageVectorTask.TASK_NAME) { task ->
            task.group = GenerateSvgImageVectorTask.TASK_GROUP
            task.dependsOn(codegenTasks)
        }

        // Run generation before any kind of kotlin source processing
        tasks.withType(AbstractKotlinCompile::class.java).configureEach {
            it.dependsOn(codegenTasks)
        }
    }

    private inline fun <reified K : KotlinProjectExtension> Project.registerTasks(
        extension: ValkyrieExtension,
        isMultiplatform: Boolean,
    ) = afterEvaluate {
        for (sourceSet in extensions.getByType(K::class.java).sourceSets) {
            // Check the source set folder's existence - if it doesn't exist it means that there aren't any kotlin, SVG
            // or drawable files to work with.
            val shouldRegisterTask = sourceSet.kotlin.srcDirs.any { it.parentFile?.exists() == true }
            if (!shouldRegisterTask) continue

            val svgTask = GenerateSvgImageVectorTask.register(project, extension, sourceSet)

            // Add to kotlin sources
            val generatedSrcDir = svgTask.get().outputDirectory
            sourceSet.kotlin.srcDir(generatedSrcDir)

            if (project.isAndroid()) {
                // Also add to Android sources. This has no effect for KMP builds, only regular Android
                extensions
                    .getByType(CommonExtension::class.java)
                    .sourceSets
                    .findByName(sourceSet.name)
                    ?.java
                    ?.srcDir(generatedSrcDir)
            }

            addDependencies(sourceSet, isMultiplatform)
        }
    }
}
