import io.github.composegears.valkyrie.internal.kotlinMultiplatformPluginId
import java.io.File
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

// custom fork of https://github.com/goncalossilva/kotlinx-resources
class WasmResourcesPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        plugins.withId(kotlinMultiplatformPluginId) {
            project.extensions
                .getByType<KotlinMultiplatformExtension>()
                .targets
                .configureEach {
                    if (platformType == KotlinPlatformType.wasm) {
                        val kotlinCompilation = compilations.getByName(KotlinCompilation.TEST_COMPILATION_NAME)

                        if (kotlinCompilation.isJsBrowserCompilation()) {
                            setupCopyResourcesTask(
                                kotlinCompilation = kotlinCompilation,
                                outputDir = kotlinCompilation.npmProject.dir.map(Directory::getAsFile),
                                mustRunAfterTasks = listOf(
                                    kotlinCompilation.processResourcesTaskName,
                                    ":${kotlinCompilation.npmProject.nodeJsRoot.npmInstallTaskProvider.name}",
                                ),
                                dependantTasks = listOf(kotlinCompilation.compileKotlinTaskName),
                            )

                            setupProxyResourcesTask(
                                kotlinCompilation = kotlinCompilation,
                                mustRunAfterTask = kotlinCompilation.processResourcesTaskName,
                                dependantTask = kotlinCompilation.compileKotlinTaskName,
                            )
                        }
                    }
                }
        }
    }

    private fun setupCopyResourcesTask(
        kotlinCompilation: KotlinCompilation<*>,
        outputDir: Provider<File>,
        mustRunAfterTasks: List<String>,
        dependantTasks: List<String>,
    ): TaskProvider<Copy>? {
        val project = kotlinCompilation.target.project
        val tasks = project.tasks

        val copyResourcesTask = tasks.register<Copy>("copyResourcesWasmJs") {
            from("${project.rootDir}/components/test/sharedTestResources")
            include("*/**")
            into(outputDir)
            mustRunAfter(*mustRunAfterTasks.toTypedArray())
        }

        dependantTasks.forEach { taskName ->
            tasks.named(taskName).configure { dependsOn(copyResourcesTask) }
        }

        return copyResourcesTask
    }

    private fun setupProxyResourcesTask(
        kotlinCompilation: KotlinCompilation<*>,
        mustRunAfterTask: String,
        dependantTask: String,
    ): TaskProvider<Task> {
        val project = kotlinCompilation.target.project
        val tasks = project.tasks
        val confFile = project.projectDir
            .resolve("karma.config.d")
            .apply { mkdirs() }
            .resolve("proxy-resources.js")

        val proxyResourcesTask = tasks.register("proxyResourcesWasmJs") {
            doLast(
                Action {
                    // Create karma configuration file in the expected location, deleting when done.
                    confFile.printWriter().use { confWriter ->
                        confWriter.println(generateKarmaConfig())
                    }
                },
            )
            mustRunAfter(mustRunAfterTask)
        }
        tasks.named(dependantTask).configure {
            dependsOn(proxyResourcesTask)
        }

        val cleanupConfFileTask = tasks.register<Delete>("proxyResourcesWasmJsCleanup") {
            delete = setOf(confFile)
        }
        tasks.named("wasmJsBrowserTest") {
            finalizedBy(cleanupConfFileTask)
        }

        return proxyResourcesTask
    }
}

private fun generateKarmaConfig(): String = """
        config.files.push({
           pattern: __dirname + "/**",
           watched: false,
           included: false,
           served: true,
           nocache: false
        });

        config.set({
            "proxies": {
               "/": __dirname + "/"
            },
            "urlRoot": "/__karma__/"
        });
    """.trimIndent()


@OptIn(ExperimentalContracts::class)
private fun KotlinCompilation<*>.isJsBrowserCompilation(): Boolean {
    contract {
        returns(true) implies (this@isJsBrowserCompilation is KotlinJsIrCompilation)
    }
    return this is KotlinJsIrCompilation && target.isBrowserConfigured
}
