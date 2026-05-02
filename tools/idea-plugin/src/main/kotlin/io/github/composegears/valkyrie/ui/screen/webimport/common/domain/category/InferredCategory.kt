package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category

/**
 * Category inferred from icon names and tags using keyword matching.
 */
data class InferredCategory(
    val id: String,
    override val name: String,
) : WebCategory {

    companion object {
        val All = InferredCategory(id = "all", name = "All")
    }
}
