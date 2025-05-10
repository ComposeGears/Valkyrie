package io.github.composegears.valkyrie.generator.jvm.model

@DslMarker
annotation class IconPackDsl

@IconPackDsl
fun iconpack(name: String, init: PackBuilder.() -> Unit = {}): IconPack {
    val builder = PackBuilder(name)
    builder.init()
    return builder.build()
}

@IconPackDsl
class PackBuilder(private val name: String) {
    private val nestedPacks = mutableListOf<IconPack>()

    fun pack(name: String, init: PackBuilder.() -> Unit = {}) {
        val builder = PackBuilder(name)
        builder.init()
        nestedPacks.add(builder.build())
    }

    internal fun build(): IconPack = IconPack(name, nestedPacks)
}
