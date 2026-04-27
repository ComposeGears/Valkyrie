package io.github.composegears.valkyrie.gradle

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import io.github.composegears.valkyrie.gradle.dsl.create
import io.github.composegears.valkyrie.gradle.dsl.findByType
import io.github.composegears.valkyrie.gradle.dsl.getByType
import io.github.composegears.valkyrie.gradle.dsl.withType
import io.github.composegears.valkyrie.gradle.internal.DEFAULT_GENERATED_SOURCES_DIR
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import io.github.composegears.valkyrie.gradle.internal.common.ExtensionValidator
import io.github.composegears.valkyrie.gradle.internal.common.PackageNameProvider.packageNameOrThrow
import io.github.composegears.valkyrie.gradle.internal.registerTask
import io.github.composegears.valkyrie.gradle.internal.task.GenerateImageVectorsTask
import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import org.gradle.api.Plugin
import org.gradle.api.Project
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

        // AGP 9.0+ has built-in Kotlin — kotlin("android") is no longer applied.
        // Use CommonExtension.sourceSets (stable AGP API) instead of KotlinAndroidProjectExtension
        // so that source-set enumeration works regardless of whether the external Kotlin plugin is present.
        // Only one Android plugin can be applied per module, so each callback fires at most once.
        listOf(
            "com.android.application",
            "com.android.library",
            "com.android.test",
            "com.android.dynamic-feature",
        ).forEach { id ->
            pluginManager.withPlugin(id) { registerAndroidTasks(extension) }
        }

        val codegenTasks = tasks.withType<GenerateImageVectorsTask>()

        // AGP's ExtractAnnotations tasks scan all source directories contributing to a variant, including
        // the generated ones registered via addStaticSourceDirectory. Because addStaticSourceDirectory
        // doesn't declare a producer-task relationship, Gradle's strict dependency validation (used with
        // --configuration-cache) raises an "implicit dependency" error.  Declaring an explicit dependsOn
        // here ensures the files are generated before any extractAnnotations task reads them.
        tasks.matching { it.name.startsWith("extract") && it.name.endsWith("Annotations") }
            .configureEach { it.dependsOn(codegenTasks) }

        // Run generation immediately if we're syncing Intellij/Android Studio - helps to speed up dev cycle
        afterEvaluate {
            ExtensionValidator.validate(extension)

            val isIdeSyncing = System.getProperty("idea.sync.active") == "true"
            if (extension.generateAtSync.getOrElse(false) && isIdeSyncing) {
                tasks.findByName("prepareKotlinIdeaImport")?.dependsOn(codegenTasks)
            }
        }

        // Run generation before any kind of kotlin source processing
        tasks.withType<AbstractKotlinCompile<*>>().configureEach { compileTask ->
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

    /**
     * Registers generation tasks for all Android source sets using the stable [CommonExtension] API.
     *
     * In AGP 9.0 with built-in Kotlin (no external kotlin("android") plugin):
     * - Phase 1: enumerate source sets via [CommonExtension.sourceSets] and register one
     *   [GenerateImageVectorsTask] per source set with its output directory set via convention.
     * - Phase 2: use [AndroidComponentsExtension.onVariants] to wire each variant's generated
     *   source directory via [com.android.build.api.variant.SourceDirectories.addStaticSourceDirectory].
     *   We deliberately use addStaticSourceDirectory (not addGeneratedSourceDirectory) because
     *   addGeneratedSourceDirectory overrides the task's outputDirectory property, which would
     *   redirect generated files away from the user-visible path
     *   (build/generated/sources/valkyrie/<sourceSet>/kotlin).
     *   The compile → gen task dependency is declared separately via AbstractKotlinCompile.dependsOn.
     */
    @Suppress("UNCHECKED_CAST")
    private fun Project.registerAndroidTasks(extension: ValkyrieExtension) {
        val androidExt = extensions.findByType<CommonExtension>() ?: return
        val androidComponents = extensions.findByType<AndroidComponentsExtension<*, *, *>>() ?: return

        // Phase 1: register one task per source set (no srcDir — wired in phase 2)
        androidExt.sourceSets.configureEach { sourceSet ->
            registerTask(
                project = this,
                extension = extension,
                sourceSetName = sourceSet.name,
                addGeneratedSrcDir = { /* handled via addStaticSourceDirectory in phase 2 */ },
            )
        }

        // Phase 2: wire the generated output directories into each variant's Kotlin compilation.
        // We use addStaticSourceDirectory (not addGeneratedSourceDirectory) so that AGP does NOT
        // override the task's outputDirectory convention — the files must land at the path our tests
        // and users expect (build/generated/sources/valkyrie/<sourceSet>/kotlin).
        // The compile → gen task dependency is already declared globally via
        // AbstractKotlinCompile.dependsOn(codegenTasks) above.
        androidComponents.onVariants { variant ->
            // Source sets that contribute to this variant:
            // "main" + buildType (e.g. "debug") + flavors (e.g. "free") + combined (e.g. "freeDebug")
            val contributingSourceSets = buildList {
                add("main")
                variant.buildType?.let { add(it) }
                variant.productFlavors.forEach { (_, flavorName) -> add(flavorName) }
                if (variant.productFlavors.isNotEmpty()) add(variant.name)
            }

            contributingSourceSets
                .filter { sourceSetName ->
                    // Only wire tasks that were actually registered (safe guard)
                    "${TASK_NAME}${sourceSetName.capitalized()}" in tasks.names
                }
                .forEach { sourceSetName ->
                    val genDir = extension.outputDirectory
                        .get()
                        .dir("$sourceSetName/kotlin")
                        .asFile
                        .absolutePath
                    variant.sources.kotlin?.addStaticSourceDirectory(genDir)
                }
        }
    }
}
