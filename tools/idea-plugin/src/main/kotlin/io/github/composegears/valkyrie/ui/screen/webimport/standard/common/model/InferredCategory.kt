package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.WebCategory

/**
 * Category inferred from icon names and tags using keyword matching.
 * Used by icon sources that don't provide explicit category metadata (e.g., Bootstrap, Lucide).
 */
data class InferredCategory(
    val id: String,
    override val name: String,
) : WebCategory {
    companion object {
        val All = InferredCategory(id = "all", name = "All")
    }
}
