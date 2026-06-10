package io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree

@DslMarker
annotation class IconPackDsl

@IconPackDsl
fun iconPackTree(name: String, init: PackBuilder.() -> Unit = {}): IconPackTree {
    val builder = PackBuilder(name)
    builder.init()
    return builder.build()
}

@IconPackDsl
class PackBuilder(private val name: String) {
    private val nestedPacks = mutableListOf<IconPackTree>()

    fun pack(name: String, init: PackBuilder.() -> Unit = {}) {
        val builder = PackBuilder(name)
        builder.init()
        nestedPacks.add(builder.build())
    }

    internal fun build(): IconPackTree = IconPackTree(
        data = name,
        children = nestedPacks,
    )
}
