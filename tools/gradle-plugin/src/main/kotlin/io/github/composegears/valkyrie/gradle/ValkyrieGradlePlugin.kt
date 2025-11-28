package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.gradle.dsl.create
import io.github.composegears.valkyrie.gradle.dsl.getByType
import io.github.composegears.valkyrie.gradle.dsl.withType
import io.github.composegears.valkyrie.gradle.internal.DEFAULT_GENERATED_SOURCES_DIR
import io.github.composegears.valkyrie.gradle.internal.GenerateImageVectorsTask
import io.github.composegears.valkyrie.gradle.internal.PackageNameProvider.packageNameOrThrow
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import io.github.composegears.valkyrie.gradle.internal.registerTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

@Suppress("unused") // Registered as a Gradle plugin.
class ValkyrieGradlePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val extension = extensions.create<ValkyrieExtension>("valkyrie").apply {
            // should be registered here for configuration cache
            packageName.convention(packageNameOrThrow())
            outputDirectory.convention(layout.buildDirectory.dir(DEFAULT_GENERATED_SOURCES_DIR))
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            registerTasks<KotlinJvmProjectExtension>(extension)
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            registerTasks<KotlinMultiplatformExtension>(extension)
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.android") {
            pluginManager.withPlugin("com.android.base") {
                registerTasks<KotlinAndroidProjectExtension>(extension)
            }
        }

        val codegenTasks = tasks.withType<GenerateImageVectorsTask>()

        // Run generation immediately if we're syncing Intellij/Android Studio - helps to speed up dev cycle
        afterEvaluate {
            val isIdeSyncing = System.getProperty("idea.sync.active") == "true"
            if (extension.generateAtSync.getOrElse(false) && isIdeSyncing) {
                tasks.findByName("prepareKotlinIdeaImport")?.dependsOn(codegenTasks)
            }
        }

        // Run generation before any kind of kotlin source processing
        tasks.withType(AbstractKotlinCompile::class.java).configureEach { compileTask ->
            compileTask.dependsOn(codegenTasks)
        }

        // Create a wrapper task to invoke all other codegen tasks
        tasks.register(TASK_NAME) { task ->
            task.dependsOn(codegenTasks)
        }
    }

    private inline fun <reified T : KotlinSourceSetContainer> Project.registerTasks(extension: ValkyrieExtension) {
        extensions.getByType<T>().sourceSets.configureEach { sourceSet ->
            registerTask(project, extension, sourceSet)
        }
    }
}
