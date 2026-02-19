package io.github.composegears.valkyrie.util.font

/**
 * Decodes WOFF2 font files to TTF/OTF format using the Google woff2 native library via JNI.
 *
 * The underlying C++ implementation statically links:
 * - [google/woff2](https://github.com/google/woff2) (MIT license)
 * - [google/brotli](https://github.com/google/brotli) (MIT license)
 *
 * Supported platforms: macOS (arm64/x86_64), Linux (x86_64), Windows (x86_64).
 */
object Woff2Decoder {

    init {
        NativeLibraryLoader.load("woff2decoder")
    }

    /**
     * Decodes WOFF2 font bytes to TTF/OTF format.
     *
     * @param inBytes WOFF2 font file contents as a byte array
     * @return TTF/OTF font bytes, or `null` if decompression fails
     */
    external fun decodeBytes(inBytes: ByteArray): ByteArray?
}
