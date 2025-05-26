package io.github.composegears.valkyrie.gradle

import org.gradle.api.Action
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

abstract class ValkyrieExtension {
    /**
     * Package name of the generated accessors. If you have any Android gradle plugin applied, this will default to
     * the [com.android.build.api.dsl.CommonExtension.namespace] property - or fail otherwise.
     */
    abstract val packageName: Property<String>

    /**
     * When true, accessor generation will be re-run when clicking Sync in the IntelliJ IDE UI. Disabled by default.
     */
    abstract val generateAtSync: Property<Boolean>

    /**
     * Source set to dump the generated classes into. Defaults to "main", unless you're using Kotlin Multiplatform in
     * which case it defaults to "commonMain".
     */
    abstract val outputSourceSet: Property<String>

    /**
     * Output location of the generated files. Defaults to "<project-dir>/build/generated/sources/valkyrie".
     */
    abstract val outputDirectory: DirectoryProperty

    /**
     * Adjusts the various configuration options. See [ValkyrieConfig] for default parameters.
     */
    fun configure(action: Action<ValkyrieConfig>) {
        action.execute(config)
    }

    internal val config = ValkyrieConfig()
}
