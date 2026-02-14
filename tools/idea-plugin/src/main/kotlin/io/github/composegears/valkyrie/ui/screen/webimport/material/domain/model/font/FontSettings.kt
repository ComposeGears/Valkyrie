package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconSettings

data class FontSettings(
    val fill: Boolean = false,
    val weight: Int = 400,
    val grade: Int = 0,
    val opticalSize: Float = 24f,
    val iconFontFamily: IconFontFamily = IconFontFamily.OUTLINED,
) : IconSettings {
    init {
        require(weight in 100..700) { "Weight must be between 100 and 700" }
        require(grade in -25..200) { "Grade must be between -25 and 200" }
        require(opticalSize in 20f..48f) { "Optical size must be between 20 and 48" }
    }

    override val isModified: Boolean
        get() = fill || weight != 400 || grade != 0 || opticalSize != 24f
}
