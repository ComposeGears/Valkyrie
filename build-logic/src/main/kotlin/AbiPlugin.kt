import io.github.composegears.valkyrie.internal.kmpExtension
import io.github.composegears.valkyrie.internal.kotlinJvm
import io.github.composegears.valkyrie.internal.kotlinJvmPluginId
import io.github.composegears.valkyrie.internal.kotlinMultiplatformPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

class AbiPlugin : Plugin<Project> {

    @OptIn(ExperimentalAbiValidation::class)
    override fun apply(target: Project) = with(target) {
        plugins.withId(kotlinMultiplatformPluginId) {
            kmpExtension {
                abiValidation()
                configureAbiTask()
            }
        }
        plugins.withId(kotlinJvmPluginId) {
            kotlinJvm {
                abiValidation()
                configureAbiTask()
            }
        }
    }

    private fun Project.configureAbiTask() {
        tasks.named("check") {
            dependsOn(tasks.named("checkKotlinAbi"))
        }
    }
}
