package io.github.composegears.valkyrie.ui.screen.webimport.standard.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.WebIcon

/**
 * Standard icon model for icon providers that use inferred categories.
 *
 * @property name The raw icon name/id used for API calls
 * @property displayName The human-readable name shown in the UI
 * @property exportName The canonical name used for generated symbol/file names
 * @property codepoint The Unicode codepoint for font-based icons
 * @property tags List of tags associated with the icon (may be empty for some providers)
 * @property category The inferred category based on name/tags analysis
 * @property style Optional style variant for providers that separate icon sets by style
 */
data class StandardIcon(
    override val name: String,
    val displayName: String,
    val exportName: String = name,
    val codepoint: Int,
    val tags: List<String>,
    val category: InferredCategory,
    val style: IconStyle? = null,
) : WebIcon
