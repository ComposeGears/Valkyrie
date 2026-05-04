package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data

fun resolveOcticonsSvgUrl(path: String, version: String): String {
    return "https://cdn.jsdelivr.net/npm/@primer/octicons@$version/build/svg/${path.trimStart('/')}"
}
