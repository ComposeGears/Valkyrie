
import io.github.composegears.valkyrie.internal.composeCompiler
import io.github.composegears.valkyrie.internal.jetbrainsComposePluginId
import io.github.composegears.valkyrie.internal.kotlinComposePluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = jetbrainsComposePluginId)
        apply(plugin = kotlinComposePluginId)

        composeCompiler {
            stabilityConfigurationFiles.add {
                rootDir.resolve("stability_config.conf")
            }
        }
    }
}
