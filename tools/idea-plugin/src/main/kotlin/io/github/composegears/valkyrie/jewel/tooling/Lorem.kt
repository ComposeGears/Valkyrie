package io.github.composegears.valkyrie.jewel.tooling

import kotlin.random.Random

private val LOREM_IPSUM_SOURCE =
    """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales
laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu
elit ut nunc convallis laoreet non ut libero. Suspendisse interdum placerat
risus vel ornare. Donec vehicula, turpis sed consectetur ullamcorper, ante
nunc egestas quam, ultricies adipiscing velit enim at nunc. Aenean id diam
neque. Praesent ut lacus sed justo viverra fermentum et ut sem. Fusce
convallis gravida lacinia. Integer semper dolor ut elit sagittis lacinia.
Praesent sodales scelerisque eros at rhoncus. Duis posuere sapien vel ipsum
ornare interdum at eu quam. Vestibulum vel massa erat. Aenean quis sagittis
purus. Phasellus arcu purus, rutrum id consectetur non, bibendum at nibh.

Duis nec erat dolor. Nulla vitae consectetur ligula. Quisque nec mi est. Ut
quam ante, rutrum at pellentesque gravida, pretium in dui. Cras eget sapien
velit. Suspendisse ut sem nec tellus vehicula eleifend sit amet quis velit.
Phasellus quis suscipit nisi. Nam elementum malesuada tincidunt. Curabitur
iaculis pretium eros, malesuada faucibus leo eleifend a. Curabitur congue
orci in neque euismod a blandit libero vehicula.
"""
        .trim()
        .split(Regex("\\s+"))

fun randomLorem(): String = generateLoremIpsum(Random.nextInt(3, 25))

fun lorem(words: Int): String = generateLoremIpsum(words)

private fun generateLoremIpsum(words: Int): String {
    var wordsUsed = 0
    val loremIpsumMaxSize = LOREM_IPSUM_SOURCE.size
    return generateSequence { LOREM_IPSUM_SOURCE[wordsUsed++ % loremIpsumMaxSize] }
        .take(words)
        .joinToString(" ")
}
