package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon

import androidx.compose.runtime.Immutable
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.Codepoint

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
@Immutable
data class StandardIcon(
    override val name: String,
    val displayName: String,
    val exportName: String = name,
    val codepoint: Codepoint,
    val tags: List<String>,
    override val category: InferredCategory,
    override val style: IconStyle? = null,
) : StyledWebIcon
