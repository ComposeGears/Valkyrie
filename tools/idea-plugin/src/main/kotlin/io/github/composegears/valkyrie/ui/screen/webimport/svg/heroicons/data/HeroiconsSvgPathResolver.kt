package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data

fun resolveHeroiconsSvgUrl(path: String, version: String): String {
    return "https://cdn.jsdelivr.net/npm/heroicons@$version/${path.trimStart('/')}"
}
