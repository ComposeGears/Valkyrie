package io.github.composegears.valkyrie.processing.writter

import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.writeText

object FileWriter {

  @Throws(IOException::class)
  fun writeToFile(
    content: String,
    outDirectory: String,
    fileName: String,
    deleteIfExists: Boolean = true,
    createParent: Boolean = true,
  ) {
    val outPath = Path(outDirectory, "$fileName.kt")

    if (deleteIfExists && outPath.exists()) {
      outPath.deleteIfExists()
    }
    if (createParent) {
      outPath.createParentDirectories()
    }
    outPath.writeText(content)
  }
}
