package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.WebIcon

data class LucideIcon(
    override val name: String,
    val displayName: String,
    val codepoint: Int,
    val tags: List<String>,
    val category: Category,
) : WebIcon
