package io.github.composegears.valkyrie.util.font

import java.io.File
import java.io.FileNotFoundException

/**
 * Loads platform-specific native libraries from JAR resources.
 *
 * Native binaries are stored under `/native/{platform}/` in the JAR:
 * - `native/macos/libwoff2decoder.dylib` (universal: arm64 + x86_64)
 * - `native/linux-x64/libwoff2decoder.so`
 * - `native/windows-x64/woff2decoder.dll`
 */
internal object NativeLibraryLoader {

    private val loaded = mutableSetOf<String>()

    @Synchronized
    fun load(libName: String) {
        if (libName in loaded) return

        val platform = detectPlatform()
        val fileName = System.mapLibraryName(libName)
        val resourcePath = "/native/$platform/$fileName"

        val resourceStream = NativeLibraryLoader::class.java.getResourceAsStream(resourcePath)
            ?: throw FileNotFoundException(
                "Native library not found for platform '$platform': $resourcePath",
            )

        val tempDir = File(System.getProperty("java.io.tmpdir"), "valkyrie-native")
        tempDir.mkdirs()

        val tempFile = File(tempDir, fileName)
        resourceStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        tempFile.deleteOnExit()

        System.load(tempFile.absolutePath)
        loaded.add(libName)
    }

    private fun detectPlatform(): String {
        val os = System.getProperty("os.name").lowercase()
        val arch = System.getProperty("os.arch").lowercase()

        return when {
            os.contains("mac") || os.contains("darwin") -> "macos"
            os.contains("linux") -> when {
                arch == "amd64" || arch == "x86_64" -> "linux-x64"
                else -> error("Unsupported Linux architecture: $arch")
            }
            os.contains("windows") -> when {
                arch == "amd64" || arch == "x86_64" -> "windows-x64"
                else -> error("Unsupported Windows architecture: $arch")
            }
            else -> error("Unsupported operating system: $os")
        }
    }
}
