package io.github.composegears.valkyrie.gradle

import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

open class ValkyrieExtension @Inject constructor(objects: ObjectFactory, layout: ProjectLayout) {
    /**
     * Package name of the generated accessors. If you have any Android gradle plugin applied, this will default to
     * the [com.android.build.api.dsl.CommonExtension.namespace] property - or fail otherwise.
     */
    val packageName: Property<String> = objects
        .property<String>()
        .unsetConvention()

    /**
     * When true, accessor generation will be re-run when clicking Sync in the IntelliJ IDE UI. Disabled by default.
     */
    val generateAtSync: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)

    /**
     * Source set to dump the generated classes into. Defaults to "main", unless you're using Kotlin Multiplatform in
     * which case it defaults to "commonMain".
     */
    val outputSourceSet: Property<String> = objects
        .property<String>()
        .unsetConvention()

    /**
     * Output location of the generated files. Defaults to "<project-dir>/build/generated/sources/valkyrie".
     */
    val outputDirectory: DirectoryProperty = objects
        .directoryProperty()
        .convention(layout.buildDirectory.dir("generated/sources/valkyrie"))

    /**
     * Adjusts the various configuration options. See [ValkyrieConfig] for default parameters.
     */
    fun configure(action: Action<ValkyrieConfig>) {
        action.execute(config)
    }

    internal val config = ValkyrieConfig()
}
