package io.github.composegears.valkyrie.util.font

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.zip.Inflater
import kotlin.math.ln

/**
 * Converts WOFF (Web Open Font Format v1) to TTF/OTF format.
 *
 */
object WoffToTtfConverter {

    private const val WOFF_SIGNATURE = 0x774F4646

    /**
     * Converts WOFF font bytes to TTF/OTF format.
     *
     * @param woffBytes The WOFF font file as a byte array
     * @return The converted TTF/OTF font as a byte array
     * @throws IllegalArgumentException if the input is not a valid WOFF file
     */
    fun convert(woffBytes: ByteArray): ByteArray {
        val input = DataInputStream(ByteArrayInputStream(woffBytes))
        val header = parseHeader(input)
        val tableEntries = parseTableDirectory(input, header.numTables)

        return buildTtf(header.flavor, tableEntries, woffBytes)
    }

    private fun parseHeader(input: DataInputStream): WoffHeader {
        val signature = input.readInt()
        require(signature == WOFF_SIGNATURE) { "Not a WOFF file: invalid signature" }

        val flavor = input.readInt()
        input.readInt() // skip length (unused)
        val numTables = input.readUnsignedShort()
        input.readUnsignedShort() // reserved
        // Skip remaining header fields (totalSfntSize, version, metadata, private data) since we don't use them
        repeat(7) { input.readInt() }

        return WoffHeader(flavor, numTables)
    }

    private fun parseTableDirectory(input: DataInputStream, numTables: Int): List<WoffTableEntry> {
        return List(numTables) {
            val tagBytes = ByteArray(4)
            input.readFully(tagBytes)

            WoffTableEntry(
                tag = String(tagBytes, Charsets.ISO_8859_1),
                offset = input.readInt().toUInt().toLong(),
                compressedLength = input.readInt().toUInt().toLong(),
                originalLength = input.readInt().toUInt().toLong(),
            ).also { input.readInt() } // skip checksum
        }
    }

    private fun buildTtf(
        flavor: Int,
        tableEntries: List<WoffTableEntry>,
        woffBytes: ByteArray,
    ): ByteArray {
        val output = ByteArrayOutputStream()
        val dataOutput = DataOutputStream(output)

        val decompressedTables = tableEntries.map { entry ->
            val compressedData = woffBytes.copyOfRange(
                fromIndex = entry.offset.toInt(),
                toIndex = (entry.offset + entry.compressedLength).toInt(),
            )
            entry to decompressTable(compressedData, entry.originalLength.toInt())
        }

        // Calculate table offsets (SFNT header = 12 bytes, each directory entry = 16 bytes)
        val tableOffsets = mutableListOf<Int>()
        var currentOffset = 12 + tableEntries.size * 16
        for ((_, data) in decompressedTables) {
            tableOffsets.add(currentOffset)
            currentOffset += data.size.alignTo4()
        }

        // Write SFNT header
        val numTables = tableEntries.size
        val searchRange = highestPowerOf2(numTables) * 16
        val entrySelector = log2(highestPowerOf2(numTables))

        dataOutput.apply {
            writeInt(flavor)
            writeShort(numTables)
            writeShort(searchRange)
            writeShort(entrySelector)
            writeShort(numTables * 16 - searchRange)
        }

        // Write table directory
        decompressedTables.forEachIndexed { index, (entry, data) ->
            dataOutput.apply {
                writeBytes(entry.tag)
                writeInt(calculateChecksum(data))
                writeInt(tableOffsets[index])
                writeInt(data.size)
            }
        }

        // Write table data with 4-byte alignment
        for ((_, data) in decompressedTables) {
            dataOutput.write(data)
            repeat((4 - data.size % 4) % 4) { dataOutput.write(0) }
        }

        return output.toByteArray()
    }

    private fun decompressTable(compressedData: ByteArray, expectedSize: Int): ByteArray {
        // Uncompressed if sizes match
        if (compressedData.size == expectedSize) return compressedData

        val inflater = Inflater()
        try {
            inflater.setInput(compressedData)
            val output = ByteArrayOutputStream(expectedSize)
            val buffer = ByteArray(4096)

            while (!inflater.finished()) {
                output.write(buffer, 0, inflater.inflate(buffer))
            }

            return output.toByteArray().also {
                require(it.size == expectedSize) {
                    "Decompressed size mismatch: expected $expectedSize, got ${it.size}"
                }
            }
        } finally {
            inflater.end()
        }
    }

    private fun Int.alignTo4(): Int = (this + 3) and -4

    private fun highestPowerOf2(n: Int): Int {
        var power = 1
        while (power * 2 <= n) power *= 2
        return power
    }

    private fun log2(n: Int): Int = (ln(n.toDouble()) / ln(2.0)).toInt()

    private fun calculateChecksum(data: ByteArray): Int {
        val padded = if (data.size % 4 != 0) data + ByteArray(4 - data.size % 4) else data
        var sum = 0L
        for (i in padded.indices step 4) {
            sum += ((padded[i].toLong() and 0xFF) shl 24) or
                ((padded[i + 1].toLong() and 0xFF) shl 16) or
                ((padded[i + 2].toLong() and 0xFF) shl 8) or
                (padded[i + 3].toLong() and 0xFF)
        }
        return sum.toInt()
    }
}

private data class WoffHeader(val flavor: Int, val numTables: Int)

private data class WoffTableEntry(
    val tag: String,
    val offset: Long,
    val compressedLength: Long,
    val originalLength: Long,
)
