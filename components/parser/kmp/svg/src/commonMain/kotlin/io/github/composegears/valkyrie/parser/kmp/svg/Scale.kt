package io.github.composegears.valkyrie.parser.kmp.svg

import kotlin.jvm.JvmInline

@JvmInline
internal value class Scale(private val args: List<Float>) {
    companion object {
        val Default = Scale(listOf(1f, 1f))
    }

    val x get() = args[0]
    val y get() = args.getOrElse(1) { x }
}
