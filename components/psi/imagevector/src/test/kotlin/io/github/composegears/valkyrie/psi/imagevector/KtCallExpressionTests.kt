package io.github.composegears.valkyrie.psi.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import io.github.composegears.valkyrie.psi.imagevector.common.getIrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KtCallExpressionTests {
    @Nested
    inner class FromColorName {
        @Test
        fun `Black to 0x000000`() = assertThat(getIrColor("Black")).isEqualTo(IrColor(0xFF000000))

        @Test
        fun `DarkGray to 0x444444`() = assertThat(getIrColor("DarkGray")).isEqualTo(IrColor(0xFF444444))

        @Test
        fun `Gray to 0x888888`() = assertThat(getIrColor("Gray")).isEqualTo(IrColor(0xFF888888))

        @Test
        fun `LightGray to 0xCCCCCC`() = assertThat(getIrColor("LightGray")).isEqualTo(IrColor(0xFFCCCCCC))

        @Test
        fun `White to 0xFFFFFF`() = assertThat(getIrColor("White")).isEqualTo(IrColor(0xFFFFFFFF))

        @Test
        fun `Red to 0xFF0000`() = assertThat(getIrColor("Red")).isEqualTo(IrColor(0xFFFF0000))

        @Test
        fun `Green to 0x00FF00`() = assertThat(getIrColor("Green")).isEqualTo(IrColor(0xFF00FF00))

        @Test
        fun `Blue to 0x0000FF`() = assertThat(getIrColor("Blue")).isEqualTo(IrColor(0xFF0000FF))

        @Test
        fun `Yellow to 0xFFFF00`() = assertThat(getIrColor("Yellow")).isEqualTo(IrColor(0xFFFFFF00))

        @Test
        fun `Cyan to 0x00FFFF`() = assertThat(getIrColor("Cyan")).isEqualTo(IrColor(0xFF00FFFF))

        @Test
        fun `Magenta to 0xFF00FF`() = assertThat(getIrColor("Magenta")).isEqualTo(IrColor(0xFFFF00FF))

        @Test
        fun `Transparent to 0x00000000`() = assertThat(getIrColor("Transparent")).isEqualTo(IrColor(0x00000000))

        @Test
        fun `Black with alpha to 0x32000000`() = assertThat(getIrColor("Black.copy(alpha = 0.5f)")).isEqualTo(IrColor(0x7F000000))

        @Test
        fun `White with alpha to 0x19FFFFFF`() = assertThat(getIrColor("White.copy(alpha = 0.25f)")).isEqualTo(IrColor(0x3FFFFFFF))

        @Test
        fun `Red with alpha to 0xBFFF0000`() = assertThat(getIrColor("Red.copy(alpha = 0.75f)")).isEqualTo(IrColor(0xBFFF0000))

        @Test
        fun `empty string to null`() = assertThat(getIrColor("")).isNull()
    }
}
