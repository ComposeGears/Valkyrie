import io.github.composegears.valkyrie.internal.kover
import io.github.composegears.valkyrie.internal.koverPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class KoverPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = koverPluginId)

        kover {
            useJacoco(libs.versions.jacoco.get())
        }
    }
}
