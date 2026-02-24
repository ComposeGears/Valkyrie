package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack

private const val DEFAULT_ICON_NAME = "IconName"

object BatchIconsIssuesResolver {

    fun resolve(batchIcons: List<BatchIcon>, useFlatPackage: Boolean = false): List<BatchIcon.Valid> {
        val sanitizedIcons = batchIcons
            .filterIsInstance<BatchIcon.Valid>()
            .map { it.sanitizeName() }

        return sanitizedIcons
            .groupBy { it.outputLocationKey(useFlatPackage) }
            .flatMap { (_, iconsInLocation) -> resolveLocationDuplicates(iconsInLocation) }
    }

    private fun BatchIcon.Valid.sanitizeName(): BatchIcon.Valid {
        val name = iconName.name
        return when {
            name.isEmpty() -> copy(iconName = IconName(DEFAULT_ICON_NAME))
            name.contains(" ") -> copy(iconName = IconName(name.replace(" ", "")))
            else -> this
        }
    }

    private fun resolveLocationDuplicates(icons: List<BatchIcon.Valid>): List<BatchIcon.Valid> {
        val committedNames = UniqueNameTracker(initialNames = icons.map { it.iconName.name })

        val afterExactDedup = resolveExactDuplicates(icons, committedNames)
        return resolveCaseInsensitiveDuplicates(afterExactDedup, committedNames)
    }

    private fun resolveExactDuplicates(
        icons: List<BatchIcon.Valid>,
        committedNames: UniqueNameTracker,
    ): List<BatchIcon.Valid> {
        val nameGroups = icons.groupBy { it.iconName.name }
        val nameCounters = mutableMapOf<String, Int>()

        return icons.map { icon ->
            val originalName = icon.iconName.name
            val isDuplicate = nameGroups[originalName].orEmpty().size > 1

            if (!isDuplicate) return@map icon

            val counter = nameCounters.increment(originalName)
            if (counter == 1) return@map icon

            val uniqueName = committedNames.generateUniqueName(baseName = originalName, startSuffix = counter - 1)
            icon.copy(iconName = IconName(uniqueName))
        }
    }

    private fun resolveCaseInsensitiveDuplicates(
        icons: List<BatchIcon.Valid>,
        committedNames: UniqueNameTracker,
    ): List<BatchIcon.Valid> {
        val lowercaseGroups = icons.groupBy { it.iconName.name.lowercase() }
        val lowercaseCounters = mutableMapOf<String, Int>()

        return icons.map { icon ->
            val currentName = icon.iconName.name
            val lowercaseKey = currentName.lowercase()
            val group = lowercaseGroups[lowercaseKey].orEmpty()
            val hasCaseConflict = group.size > 1 && group.distinctBy { it.iconName.name }.size > 1

            if (!hasCaseConflict) return@map icon

            val counter = lowercaseCounters.increment(lowercaseKey)
            if (counter == 1) return@map icon

            val uniqueName = committedNames.generateUniqueName(baseName = currentName, startSuffix = counter - 1)
            icon.copy(iconName = IconName(uniqueName))
        }
    }

    private fun BatchIcon.Valid.outputLocationKey(useFlatPackage: Boolean): String {
        return when (val pack = iconPack) {
            is IconPack.Single -> pack.iconPackName
            is IconPack.Nested -> when {
                useFlatPackage -> pack.iconPackName
                else -> "${pack.iconPackName}.${pack.currentNestedPack}"
            }
        }
    }
}

private class UniqueNameTracker(initialNames: List<String>) {

    private val names = initialNames.toMutableSet()

    fun generateUniqueName(baseName: String, startSuffix: Int): String {
        var suffix = startSuffix
        var candidate = "$baseName$suffix"
        while (names.any { it.equals(candidate, ignoreCase = true) }) {
            suffix++
            candidate = "$baseName$suffix"
        }
        names.add(candidate)
        return candidate
    }
}

private fun MutableMap<String, Int>.increment(key: String): Int {
    val newValue = getOrDefault(key, 0) + 1
    this[key] = newValue
    return newValue
}
