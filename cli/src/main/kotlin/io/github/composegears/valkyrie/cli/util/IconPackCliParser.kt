package io.github.composegears.valkyrie.cli.util

import io.github.composegears.valkyrie.generator.model.IconPack

object IconPackCliParser {

    fun parse(input: String): IconPack {
        val paths = input
            .split(",")
            .map { it.split(".") }

        // Extract the root name from the first segment of all paths
        val rootSegments = paths.mapNotNull { it.firstOrNull() }.distinct()
        if (rootSegments.size != 1) {
            error("Invalid icon pack structure: expected a single root, but found ${rootSegments.size} roots")
        }

        // Group the paths by their second segment
        val secondLevelGroups = paths
            .filter { it.size > 1 }
            .groupBy { it[1] }

        // For each second-level group, create an IconPack
        val secondLevelPacks = secondLevelGroups.map { (secondLevelName, pathsInGroup) ->
            // Create third level and below
            val nestedPacks = buildNestedPacks(pathsInGroup, 2)
            IconPack(name = secondLevelName, nested = nestedPacks)
        }

        return IconPack(
            name = rootSegments.first(),
            nested = secondLevelPacks
        )
    }

    private fun buildNestedPacks(paths: List<List<String>>, depth: Int): List<IconPack> {
        val segments = paths
            .filter { it.size > depth }
            .map { it[depth] }
            .distinct()

        return segments.map { segment ->
            val pathsForSegment = paths.filter { it.size > depth && it[depth] == segment }
            val nestedPacks = buildNestedPacks(pathsForSegment, depth + 1)

            IconPack(name = segment, nested = nestedPacks)
        }
    }
}
