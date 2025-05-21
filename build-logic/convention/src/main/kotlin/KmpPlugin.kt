import io.github.composegears.valkyrie.extension.applyTargets
import io.github.composegears.valkyrie.internal.kmpExtension
import io.github.composegears.valkyrie.internal.kotlinMultiplatformPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class KmpPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = kotlinMultiplatformPluginId)

        kmpExtension {
            applyTargets()

            compilerOptions {
                extraWarnings.set(true)
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}
