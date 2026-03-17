
import io.github.composegears.valkyrie.internal.configureArchiveBaseName
import io.github.composegears.valkyrie.internal.kotlinJvmPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class JvmPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = kotlinJvmPluginId)

        dependencies {
            compileOnly(kotlin("stdlib"))
        }

        configureArchiveBaseName()
    }
}

private fun DependencyHandler.`compileOnly`(dependencyNotation: Any): Dependency? =
    add("compileOnly", dependencyNotation)
