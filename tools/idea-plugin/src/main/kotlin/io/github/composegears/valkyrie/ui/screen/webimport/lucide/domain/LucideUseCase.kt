package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain

import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.data.LucideRepository
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideConfig
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideIcon
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.font.FontByteArray

/**
 * Category keyword mapping with priority (lower number = higher priority).
 * Used for inferring icon categories from names and tags.
 *
 * Priority helps when an icon matches multiple keywords:
 * - Specific categories (e.g., "arrow-up") match "arrow" (priority 1) before "navigation" (priority 5)
 * - This ensures icons get the most relevant category
 *
 * Examples:
 * - "arrow-up" → Arrows (name contains "arrow", priority 1)
 * - "file-text" → Files (name contains "file", priority 1)
 * - "send" with tags ["mail", "email"] → Mail (tag contains "mail", priority 1)
 * - "arrow-navigation" → Arrows (name has "arrow" p1, "navigation" p5 - arrow wins)
 * - "unknown-icon" → General (no matches, fallback)
 *
 */
private val CATEGORY_KEYWORDS = listOf(
    CategoryKeyword("arrow", "Arrows", priority = 1),
    CategoryKeyword("file", "Files", priority = 1),
    CategoryKeyword("mail", "Mail", priority = 1),
    CategoryKeyword("chart", "Charts", priority = 1),
    CategoryKeyword("calendar", "Calendar", priority = 1),
    CategoryKeyword("clock", "Time", priority = 1),
    CategoryKeyword("lock", "Security", priority = 1),
    CategoryKeyword("key", "Security", priority = 1),

    CategoryKeyword("text", "Text", priority = 2),
    CategoryKeyword("user", "People", priority = 2),
    CategoryKeyword("people", "People", priority = 2),
    CategoryKeyword("building", "Buildings", priority = 2),
    CategoryKeyword("home", "Buildings", priority = 2),
    CategoryKeyword("device", "Devices", priority = 2),
    CategoryKeyword("phone", "Devices", priority = 2),
    CategoryKeyword("monitor", "Devices", priority = 2),
    CategoryKeyword("tool", "Tools", priority = 2),
    CategoryKeyword("settings", "Tools", priority = 2),

    CategoryKeyword("media", "Multimedia", priority = 3),
    CategoryKeyword("play", "Multimedia", priority = 3),
    CategoryKeyword("video", "Multimedia", priority = 3),
    CategoryKeyword("music", "Multimedia", priority = 3),
    CategoryKeyword("shape", "Shapes", priority = 3),
    CategoryKeyword("circle", "Shapes", priority = 3),
    CategoryKeyword("square", "Shapes", priority = 3),

    CategoryKeyword("navigation", "Navigation", priority = 5),
    CategoryKeyword("communication", "Communication", priority = 5),
    CategoryKeyword("social", "Social", priority = 5),
)

private data class CategoryKeyword(
    val keyword: String,
    val categoryName: String,
    val priority: Int,
)

class LucideUseCase(
    private val repository: LucideRepository,
) {
    suspend fun loadConfig(): LucideConfig {
        val iconMetadataList = repository.loadIconList()
        val codepoints = repository.loadCodepoints()

        val icons = iconMetadataList.mapNotNull { (name, metadata) ->
            val codepoint = codepoints[name] ?: return@mapNotNull null
            LucideIcon(
                name = name,
                displayName = name.toDisplayName(),
                codepoint = codepoint,
                tags = metadata.tags,
                category = inferCategoryFromTags(name, metadata.tags),
            )
        }

        val categories = icons
            .map { it.category }
            .distinctBy { it.id }
            .sortedBy { it.title }

        val groupedIcons = icons.groupBy { it.category }

        return LucideConfig.create(
            gridItems = groupedIcons,
            categories = listOf(Category.All) + categories,
        )
    }

    /**
     * Infers the most appropriate category for an icon based on its name and tags.
     *
     * Uses a priority-based matching system:
     * 1. Checks icon name for keyword matches (higher weight)
     * 2. Checks tags for keyword matches
     * 3. When multiple keywords match, uses the highest priority (lowest priority value)
     * 4. Falls back to "General" if no matches found
     */
    private fun inferCategoryFromTags(name: String, tags: List<String>): Category {
        val normalizedName = name.lowercase()
        val normalizedTags = tags.map { it.lowercase() }

        val nameMatches = CATEGORY_KEYWORDS
            .filter { normalizedName.contains(it.keyword) }
            .map { it.copy(priority = it.priority - 2) } // Boost priority for name matches

        val tagMatches = CATEGORY_KEYWORDS
            .filter { keyword ->
                normalizedTags.any { tag -> tag.contains(keyword.keyword) }
            }

        val allMatches = nameMatches + tagMatches
        val bestMatch = allMatches.minByOrNull { it.priority }

        return bestMatch?.toCategory() ?: Category(id = "general", title = "General")
    }

    private fun CategoryKeyword.toCategory(): Category {
        return Category(id = categoryName.lowercase(), title = categoryName)
    }

    suspend fun loadFontBytes(): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    suspend fun downloadSvg(icon: LucideIcon, settings: LucideSettings): String {
        val rawSvg = repository.downloadSvg(icon.name)
        return if (settings.isModified) {
            LucideSvgCustomizer.applySettings(rawSvg, settings)
        } else {
            rawSvg
        }
    }
}

private fun String.toDisplayName(): String {
    return split('-')
        .joinToString(separator = " ") { it.capitalized() }
}
