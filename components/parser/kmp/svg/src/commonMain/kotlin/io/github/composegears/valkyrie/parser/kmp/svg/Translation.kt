package io.github.composegears.valkyrie.parser.kmp.svg

import kotlin.jvm.JvmInline

@JvmInline
internal value class Translation(private val args: List<Float>) {
    companion object {
        val Default = Translation(listOf(0f, 0f))
    }

    val x get() = args[0]
    val y get() = args.getOrElse(1) { 0f }
}
