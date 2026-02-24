package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconSettings

data class MaterialFontSettings(
    val fill: Boolean = DEFAULT_FILL,
    val weight: Int = DEFAULT_WEIGHT,
    val grade: Int = DEFAULT_GRADE,
    val opticalSize: Float = DEFAULT_OPTICAL_SIZE,
) : IconSettings {

    companion object {
        const val DEFAULT_FILL = false
        const val DEFAULT_WEIGHT = 400
        const val DEFAULT_GRADE = 0
        const val DEFAULT_OPTICAL_SIZE = 24f
    }

    init {
        require(weight in 100..700) { "Weight must be between 100 and 700" }
        require(grade in -25..200) { "Grade must be between -25 and 200" }
        require(opticalSize in 20f..48f) { "Optical size must be between 20 and 48" }
    }

    override val isModified: Boolean
        get() = fill || weight != DEFAULT_WEIGHT || grade != DEFAULT_GRADE || opticalSize != DEFAULT_OPTICAL_SIZE
}
