package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data

fun resolveCssGgSvgUrl(path: String, version: String): String {
    return "https://cdn.jsdelivr.net/npm/css.gg@$version/${path.trimStart('/')}"
}
