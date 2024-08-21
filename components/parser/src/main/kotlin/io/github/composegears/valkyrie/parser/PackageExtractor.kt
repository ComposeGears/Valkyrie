package io.github.composegears.valkyrie.parser

import java.nio.file.Paths
import kotlin.io.path.invariantSeparatorsPathString

object PackageExtractor {

  private val knownSourceSets = listOf(
    "src/main/kotlin",
    "src/main/java",
    "src/androidMain/kotlin",
    "src/commonMain/kotlin",
    "src/desktopMain/kotlin",
    "src/jvmMain/kotlin",
    "src/jsMain/kotlin",
    "src/iosMain/kotlin",
    "src/wasmJsMain/kotlin",
    "src/linuxMain/kotlin",
    "src/macosMain/kotlin",
    "src/nativeMain/kotlin",
  )

  fun getFrom(path: String): String? {
    val invariantPath = Paths.get(path).invariantSeparatorsPathString
    val sourceSet = knownSourceSets.find { invariantPath.contains(it, ignoreCase = true) }

    return if (sourceSet != null) {
      invariantPath.substringAfter(sourceSet)
        .replace("/", ".")
        .removePrefix(".")
        .removeSuffix(".")
    } else {
      null
    }
  }
}
