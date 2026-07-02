import io.github.composegears.valkyrie.internal.kotlinJvm
import io.github.composegears.valkyrie.internal.kotlinJvmPluginId
import io.github.composegears.valkyrie.internal.kotlinMultiplatform
import io.github.composegears.valkyrie.internal.kotlinMultiplatformPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project

class ExplicitApiPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        plugins.withId(kotlinMultiplatformPluginId) {
            kotlinMultiplatform { explicitApi() }
        }
        plugins.withId(kotlinJvmPluginId) {
            kotlinJvm { explicitApi() }
        }
    }
}
