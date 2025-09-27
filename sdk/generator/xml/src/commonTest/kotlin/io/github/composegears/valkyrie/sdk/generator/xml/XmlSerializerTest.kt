package io.github.composegears.valkyrie.sdk.generator.xml

import io.github.composegears.valkyrie.sdk.core.xml.VectorDrawable
import kotlin.test.Test
import kotlin.test.assertTrue

class XmlSerializerTest {

    @Test
    fun `serialized VectorDrawable with path returns valid XML`() {
        val vectorDrawable = VectorDrawable(
            widthInDp = "24dp",
            heightInDp = "24dp",
            viewportWidth = 24f,
            viewportHeight = 24f,
            tint = "?attr/colorControlNormal",
            children = listOf(
                VectorDrawable.Path(
                    fillColor = "@android:color/white",
                    pathData = "M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2z",
                ),
            ),
        )

        val result = XmlSerializer.serialize(vectorDrawable)

        with(result) {
            assertTrue(contains("android:width=\"24dp\""))
            assertTrue(contains("android:height=\"24dp\""))
            assertTrue(contains("android:viewportWidth=\"24.0\""))
            assertTrue(contains("android:viewportHeight=\"24.0\""))
            assertTrue(contains("android:tint=\"?attr/colorControlNormal\""))
            assertTrue(contains("android:fillColor=\"@android:color/white\""))
            assertTrue(contains("android:pathData=\"M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2z\""))
        }
    }

    @Test
    fun `serialized VectorDrawable with fillType evenOdd returns XML with fillType`() {
        val vectorDrawable = VectorDrawable(
            widthInDp = "24dp",
            heightInDp = "24dp",
            viewportWidth = 24f,
            viewportHeight = 24f,
            tint = "?attr/colorControlNormal",
            children = listOf(
                VectorDrawable.Path(
                    fillType = "evenOdd",
                    fillColor = "@android:color/white",
                    pathData = "M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2z",
                ),
            ),
        )

        val result = XmlSerializer.serialize(vectorDrawable)

        assertTrue(result.contains("android:fillType=\"evenOdd\""))
        assertTrue(result.contains("android:fillColor=\"@android:color/white\""))
    }

    @Test
    fun `serialized VectorDrawable with group returns XML with group structure`() {
        val vectorDrawable = VectorDrawable(
            widthInDp = "24dp",
            heightInDp = "24dp",
            viewportWidth = 24f,
            viewportHeight = 24f,
            children = listOf(
                VectorDrawable.Group(
                    name = "rotationGroup",
                    pivotX = 10.0f,
                    pivotY = 10.0f,
                    rotation = 15.0f,
                    children = listOf(
                        VectorDrawable.Path(
                            name = "vect",
                            fillColor = "#FF000000",
                            pathData = "M15.67,4H14V2h-4v2H8.33C7.6,4 7,4.6 7,5.33V9h4.93L13,7v2h4V5.33C17,4.6 16.4,4 15.67,4z",
                            alpha = .3f,
                        ),
                        VectorDrawable.Path(
                            name = "draw",
                            fillColor = "#FF000000",
                            pathData = "M13,12.5h2L11,20v-5.5H9L11.93,9H7v11.67C7,21.4 7.6,22 8.33,22h7.33c0.74,0 1.34,-0.6 1.34,-1.33V9h-4v3.5z",
                        ),
                    ),
                ),
            ),
        )

        val result = XmlSerializer.serialize(vectorDrawable)

        with(result) {
            assertTrue(contains("android:name=\"rotationGroup\""))
            assertTrue(contains("android:pivotX=\"10.0\""))
            assertTrue(contains("android:pivotY=\"10.0\""))
            assertTrue(contains("android:rotation=\"15.0\""))
            assertTrue(contains("android:name=\"vect\""))
            assertTrue(contains("android:name=\"draw\""))
            assertTrue(contains("android:fillColor=\"#FF000000\""))
            assertTrue(contains("android:fillAlpha=\"0.3\""))
        }
    }

    @Test
    fun `serialized VectorDrawable with nested groups returns XML with hierarchical structure`() {
        val vectorDrawable = VectorDrawable(
            widthInDp = "24dp",
            heightInDp = "24dp",
            viewportWidth = 24f,
            viewportHeight = 24f,
            children = listOf(
                VectorDrawable.Group(
                    name = "parent",
                    children = listOf(
                        VectorDrawable.Group(
                            name = "parent.first",
                            children = listOf(
                                VectorDrawable.Path(pathData = "M15.67,4"),
                            ),
                        ),
                        VectorDrawable.Group(
                            name = "parent.second",
                            children = listOf(
                                VectorDrawable.Path(pathData = "M13,12.5"),
                            ),
                        ),
                    ),
                ),
            ),
        )

        val result = XmlSerializer.serialize(vectorDrawable)

        with(result) {
            assertTrue(contains("android:name=\"parent\""))
            assertTrue(contains("android:name=\"parent.first\""))
            assertTrue(contains("android:name=\"parent.second\""))
            assertTrue(contains("android:pathData=\"M15.67,4\""))
            assertTrue(contains("android:pathData=\"M13,12.5\""))
        }
    }

    @Test
    fun `serialized VectorDrawable with stroke properties returns XML with stroke attributes`() {
        val vectorDrawable = VectorDrawable(
            widthInDp = "24dp",
            heightInDp = "24dp",
            viewportWidth = 24f,
            viewportHeight = 24f,
            children = listOf(
                VectorDrawable.Path(
                    pathData = "M12,2L12,22",
                    strokeWidth = "2",
                    strokeColor = "#FF0000",
                    strokeAlpha = "0.8",
                    strokeLineCap = "round",
                    strokeLineJoin = "round",
                    strokeMiterLimit = "10",
                ),
            ),
        )

        val result = XmlSerializer.serialize(vectorDrawable)

        with(result) {
            assertTrue(contains("android:strokeWidth=\"2\""))
            assertTrue(contains("android:strokeColor=\"#FF0000\""))
            assertTrue(contains("android:strokeAlpha=\"0.8\""))
            assertTrue(contains("android:strokeLineCap=\"round\""))
            assertTrue(contains("android:strokeLineJoin=\"round\""))
            assertTrue(contains("android:strokeMiterLimit=\"10\""))
        }
    }

    @Test
    fun `serialized VectorDrawable with autoMirrored returns XML with autoMirrored attribute`() {
        val vectorDrawable = VectorDrawable(
            widthInDp = "24dp",
            heightInDp = "24dp",
            viewportWidth = 24f,
            viewportHeight = 24f,
            autoMirrored = true,
            children = listOf(
                VectorDrawable.Path(pathData = "M12,2L12,22"),
            ),
        )

        val result = XmlSerializer.serialize(vectorDrawable)

        assertTrue(result.contains("android:autoMirrored=\"true\""))
    }

    @Test
    fun `serialized VectorDrawable with name returns XML with name attribute`() {
        val vectorDrawable = VectorDrawable(
            name = "test_icon",
            widthInDp = "24dp",
            heightInDp = "24dp",
            viewportWidth = 24f,
            viewportHeight = 24f,
            children = listOf(
                VectorDrawable.Path(pathData = "M12,2L12,22"),
            ),
        )

        val result = XmlSerializer.serialize(vectorDrawable)

        assertTrue(result.contains("android:name=\"test_icon\""))
    }
}
