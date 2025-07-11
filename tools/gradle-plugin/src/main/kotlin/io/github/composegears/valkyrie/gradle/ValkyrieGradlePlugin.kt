package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.gradle.GenerateImageVectorsTask.Companion.TASK_GROUP
import io.github.composegears.valkyrie.gradle.GenerateImageVectorsTask.Companion.TASK_NAME
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
        val extension = extensions.create("valkyrie", ValkyrieExtension::class.java)

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

        val codegenTasks = tasks.withType(GenerateImageVectorsTask::class.java)

        // Run generation immediately if we're syncing Intellij/Android Studio - helps to speed up dev cycle
        val isIdeSyncing = System.getProperty("idea.sync.active") == "true"
        if (extension.generateAtSync.getOrElse(false) && isIdeSyncing) {
            tasks.findByName("prepareKotlinIdeaImport")?.dependsOn(codegenTasks)
        }

        // Run generation before any kind of kotlin source processing
        tasks.withType(AbstractKotlinCompile::class.java).configureEach { compileTask ->
            compileTask.dependsOn(codegenTasks)
        }

        // Create a wrapper task to invoke all other codegen tasks
        tasks.register(TASK_NAME) { task ->
            task.group = TASK_GROUP
            task.dependsOn(codegenTasks)
        }
    }

    private inline fun <reified T : KotlinSourceSetContainer> Project.registerTasks(extension: ValkyrieExtension) {
        extensions.getByType(T::class.java).sourceSets.configureEach { sourceSet ->
            GenerateImageVectorsTask.register(project, extension, sourceSet)
        }
    }
}
