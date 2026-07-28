package io.github.composegears.valkyrie.jewel.scroll

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlin.math.roundToInt

@Composable
fun rememberGridColumnCount(lazyGridState: LazyGridState): Int {
    val columnCount by remember(lazyGridState) {
        derivedStateOf {
            lazyGridState.layoutInfo.visibleItemsInfo
                .groupBy { it.row }
                .values
                .maxOfOrNull { it.size }
                ?: 1
        }
    }
    return columnCount
}

class SpanAwareGridScrollbarAdapter<T>(
    private val gridState: LazyGridState,
    columns: Int,
    items: List<T>,
    isHeader: (T) -> Boolean,
) : ScrollbarAdapter {

    private val lineOfIndexArr = IntArray(items.size)
    private val firstIndexOfLineArr: IntArray
    private val lineIsHeaderArr: BooleanArray
    private val headerLinesBefore: IntArray

    init {
        val firstIndices = IntArray(items.size)
        val headerFlags = BooleanArray(items.size)
        var lineCount = 0
        var line = 0
        var col = 0
        for (i in items.indices) {
            if (isHeader(items[i])) {
                if (col != 0) {
                    line++
                    col = 0
                }
                lineOfIndexArr[i] = line
                firstIndices[lineCount] = i
                headerFlags[lineCount] = true
                lineCount++
                line++
                col = 0
            } else {
                if (col == 0) {
                    firstIndices[lineCount] = i
                    headerFlags[lineCount] = false
                    lineCount++
                }
                lineOfIndexArr[i] = line
                col++
                if (col == columns) {
                    line++
                    col = 0
                }
            }
        }
        firstIndexOfLineArr = firstIndices.copyOf(lineCount)
        lineIsHeaderArr = headerFlags.copyOf(lineCount)
        headerLinesBefore = IntArray(lineIsHeaderArr.size + 1)
        for (l in lineIsHeaderArr.indices) {
            headerLinesBefore[l + 1] = headerLinesBefore[l] + if (lineIsHeaderArr[l]) 1 else 0
        }
    }

    private val totalLines
        get() = firstIndexOfLineArr.size

    private fun lineOfIndex(index: Int) = lineOfIndexArr[index]

    private fun firstIndexOfLine(line: Int) = firstIndexOfLineArr[line]

    private val layoutInfo
        get() = gridState.layoutInfo

    private val spacing
        get() = layoutInfo.mainAxisItemSpacing.toDouble()

    private var cachedHeaderHeight = 0.0
    private var cachedItemHeight = 0.0

    private fun refreshSizes() {
        for (info in layoutInfo.visibleItemsInfo) {
            val h = info.size.height.toDouble()
            if (h <= 0.0 || info.index !in lineOfIndexArr.indices) continue
            if (lineIsHeaderArr[lineOfIndexArr[info.index]]) {
                cachedHeaderHeight = h
            } else {
                cachedItemHeight = h
            }
        }
        if (cachedHeaderHeight <= 0.0) cachedHeaderHeight = cachedItemHeight
        if (cachedItemHeight <= 0.0) cachedItemHeight = cachedHeaderHeight
    }

    private val headerPitch
        get() = cachedHeaderHeight + spacing

    private val itemPitch
        get() = cachedItemHeight + spacing

    private fun yOffsetOfLine(line: Int): Double {
        val hLines = headerLinesBefore[line]
        val iLines = line - hLines
        return hLines * headerPitch + iLines * itemPitch
    }

    private fun lineOfOffset(offset: Double): Int {
        if (totalLines == 0) return 0
        var lo = 0
        var hi = totalLines - 1
        while (lo < hi) {
            val mid = (lo + hi + 1) ushr 1
            if (yOffsetOfLine(mid) <= offset) lo = mid else hi = mid - 1
        }
        return lo
    }

    override val scrollOffset: Double
        get() {
            refreshSizes()
            if (lineOfIndexArr.isEmpty()) return 0.0
            val idx = gridState.firstVisibleItemIndex.coerceIn(0, lineOfIndexArr.lastIndex)
            return yOffsetOfLine(lineOfIndex(idx)) + gridState.firstVisibleItemScrollOffset
        }

    override val contentSize: Double
        get() {
            refreshSizes()
            val lines = totalLines
            if (lines == 0) return 0.0
            val rawHeight = (yOffsetOfLine(lines) - spacing).coerceAtLeast(0.0)
            return rawHeight + layoutInfo.beforeContentPadding + layoutInfo.afterContentPadding
        }

    override val viewportSize: Double
        get() = layoutInfo.viewportSize.height.toDouble()

    override suspend fun scrollTo(scrollOffset: Double) {
        refreshSizes()
        val lines = totalLines
        if (lines == 0) return
        val maxOffset = (contentSize - viewportSize).coerceAtLeast(0.0)
        val target = scrollOffset.coerceIn(0.0, maxOffset)
        val line = lineOfOffset(target)
        val remainderPx = (target - yOffsetOfLine(line)).roundToInt().coerceAtLeast(0)
        gridState.scrollToItem(firstIndexOfLine(line), remainderPx)
    }
}
