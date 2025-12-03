package io.github.composegears.valkyrie.gradle.internal.common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal object PackageNameProvider {
    private val ANDROID_PLUGIN_IDS = listOf(
        "com.android.application",
        "com.android.library",
        "com.android.test",
        "com.android.dynamic-feature",
        // TODO: add support for "com.android.kotlin.multiplatform.library"
    )

    private val NO_PACKAGE_NAME_ERROR = """
        Couldn't automatically estimate package name - make sure to set this property in your gradle script like:

        valkyrie {
           packageName.set("my.output.package.name")
        }
    """.trimIndent()

    internal fun Project.packageNameOrThrow(): Provider<String> = provider {
        if (ANDROID_PLUGIN_IDS.any(pluginManager::hasPlugin)) {
            extensions
                .findByType(CommonExtension::class.java)
                ?.namespace
                ?: throw GradleException(NO_PACKAGE_NAME_ERROR)
        } else {
            throw GradleException(NO_PACKAGE_NAME_ERROR)
        }
    }
}
