package io.github.composegears.valkyrie.jewel.tooling

import com.intellij.codeInsight.template.emmet.generators.LoremGenerator
import kotlin.random.Random

fun randomLorem(): String = LoremGenerator().generate(Random.nextInt(3, 25), true)

fun lorem(words: Int): String = LoremGenerator().generate(words, true)
