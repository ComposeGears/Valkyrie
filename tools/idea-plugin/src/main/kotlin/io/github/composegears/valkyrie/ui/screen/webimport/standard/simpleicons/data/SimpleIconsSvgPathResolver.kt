package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data

fun resolveSimpleIconsSvgUrl(slug: String, version: String): String {
    return "https://cdn.jsdelivr.net/npm/simple-icons@$version/icons/${slug.trimStart('/')}.svg"
}
