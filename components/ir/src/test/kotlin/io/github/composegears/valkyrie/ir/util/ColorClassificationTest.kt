package io.github.composegears.valkyrie.ir.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ir.IrColor
import org.junit.jupiter.api.Test

class ColorClassificationTest {

    @Test
    fun `dominant black color`() {
        assertThat(
            actual = ColorClassification.from(listOf(IrColor("#FF000000"))),
        ).isEqualTo(DominantShade.Black)

        assertThat(
            actual = ColorClassification.from(listOf(IrColor("#FF262525"))),
        ).isEqualTo(DominantShade.Black)

        assertThat(
            actual = ColorClassification.from(
                listOf(
                    IrColor("#FF000000"),
                    IrColor("#FF000000"),
                    IrColor("#FF1A1919"),
                ),
            ),
        ).isEqualTo(DominantShade.Black)
    }

    @Test
    fun `dominant white color`() {
        val colors = listOf(
            IrColor("#FFFFFFFF"),
            IrColor("#FFFFFFFF"),
        )
        assertThat(ColorClassification.from(colors)).isEqualTo(DominantShade.White)
    }

    @Test
    fun `dominant mixed color`() {
        val colors = listOf(
            IrColor("#FFFF0000"),
            IrColor("#FF00FF00"),
            IrColor("#FF0000FF"),
        )
        assertThat(ColorClassification.from(colors)).isEqualTo(DominantShade.Mixed)
    }
}
