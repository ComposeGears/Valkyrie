package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.gradle.dsl.property
import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

abstract class CodeStyleConfigExtension @Inject constructor(objects: ObjectFactory) {
    /**
     * Add explicit `public` modifier to generated declarations.
     *
     * Default: `false`
     */
    val useExplicitMode: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)

    /**
     * Number of spaces used for each level of indentation in generated code.
     *
     * Default: `4`
     */
    val indentSize: Property<Int> = objects
        .property<Int>()
        .convention(4)
}
