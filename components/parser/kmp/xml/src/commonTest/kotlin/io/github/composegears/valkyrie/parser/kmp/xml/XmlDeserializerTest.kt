package io.github.composegears.valkyrie.parser.kmp.xml

import io.github.composegears.valkyrie.sdk.core.xml.VectorDrawable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import nl.adaptivity.xmlutil.XmlException

class XmlDeserializerTest {

    @Test
    fun `serialize valid VectorDrawable without FillType returns serialized VectorDrawable`() {
        val vector = """
        <vector xmlns:android="http://schemas.android.com/apk/res/android"
            android:width="24dp"
            android:height="24dp"
            android:viewportWidth="24"
            android:viewportHeight="24"
            android:tint="?attr/colorControlNormal">
          <path
              android:fillColor="@android:color/white"
              android:pathData="M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2zM18.92,8h-2.95c-0.32,-1.25 -0.78,-2.45 -1.38,-3.56 1.84,0.63 3.37,1.91 4.33,3.56zM12,4.04c0.83,1.2 1.48,2.53 1.91,3.96h-3.82c0.43,-1.43 1.08,-2.76 1.91,-3.96zM4.26,14C4.1,13.36 4,12.69 4,12s0.1,-1.36 0.26,-2h3.38c-0.08,0.66 -0.14,1.32 -0.14,2 0,0.68 0.06,1.34 0.14,2L4.26,14zM5.08,16h2.95c0.32,1.25 0.78,2.45 1.38,3.56 -1.84,-0.63 -3.37,-1.9 -4.33,-3.56zM8.03,8L5.08,8c0.96,-1.66 2.49,-2.93 4.33,-3.56C8.81,5.55 8.35,6.75 8.03,8zM12,19.96c-0.83,-1.2 -1.48,-2.53 -1.91,-3.96h3.82c-0.43,1.43 -1.08,2.76 -1.91,3.96zM14.34,14L9.66,14c-0.09,-0.66 -0.16,-1.32 -0.16,-2 0,-0.68 0.07,-1.35 0.16,-2h4.68c0.09,0.65 0.16,1.32 0.16,2 0,0.68 -0.07,1.34 -0.16,2zM14.59,19.56c0.6,-1.11 1.06,-2.31 1.38,-3.56h2.95c-0.96,1.65 -2.49,2.93 -4.33,3.56zM16.36,14c0.08,-0.66 0.14,-1.32 0.14,-2 0,-0.68 -0.06,-1.34 -0.14,-2h3.38c0.16,0.64 0.26,1.31 0.26,2s-0.1,1.36 -0.26,2h-3.38z"/>
        </vector>
        """.trimIndent()
        assertEquals(
            expected = VectorDrawable(
                widthInDp = "24dp",
                heightInDp = "24dp",
                viewportWidth = 24f,
                viewportHeight = 24f,
                tint = "?attr/colorControlNormal",
                children = listOf(
                    VectorDrawable.Path(
                        fillType = "nonZero",
                        fillColor = "@android:color/white",
                        pathData = "M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2zM18.92,8h-2.95c-0.32,-1.25 -0.78,-2.45 -1.38,-3.56 1.84,0.63 3.37,1.91 4.33,3.56zM12,4.04c0.83,1.2 1.48,2.53 1.91,3.96h-3.82c0.43,-1.43 1.08,-2.76 1.91,-3.96zM4.26,14C4.1,13.36 4,12.69 4,12s0.1,-1.36 0.26,-2h3.38c-0.08,0.66 -0.14,1.32 -0.14,2 0,0.68 0.06,1.34 0.14,2L4.26,14zM5.08,16h2.95c0.32,1.25 0.78,2.45 1.38,3.56 -1.84,-0.63 -3.37,-1.9 -4.33,-3.56zM8.03,8L5.08,8c0.96,-1.66 2.49,-2.93 4.33,-3.56C8.81,5.55 8.35,6.75 8.03,8zM12,19.96c-0.83,-1.2 -1.48,-2.53 -1.91,-3.96h3.82c-0.43,1.43 -1.08,2.76 -1.91,3.96zM14.34,14L9.66,14c-0.09,-0.66 -0.16,-1.32 -0.16,-2 0,-0.68 0.07,-1.35 0.16,-2h4.68c0.09,0.65 0.16,1.32 0.16,2 0,0.68 -0.07,1.34 -0.16,2zM14.59,19.56c0.6,-1.11 1.06,-2.31 1.38,-3.56h2.95c-0.96,1.65 -2.49,2.93 -4.33,3.56zM16.36,14c0.08,-0.66 0.14,-1.32 0.14,-2 0,-0.68 -0.06,-1.34 -0.14,-2h3.38c0.16,0.64 0.26,1.31 0.26,2s-0.1,1.36 -0.26,2h-3.38z",
                    ),
                ),
            ),
            actual = XmlDeserializer.deserialize(vector),
        )
    }

    @Test
    fun `serialize valid VectorDrawable with fillType evenOdd returns serialized VectorDrawable`() {
        val vector = """
        <vector xmlns:android="http://schemas.android.com/apk/res/android"
            android:width="24dp"
            android:height="24dp"
            android:viewportWidth="24"
            android:viewportHeight="24"
            android:tint="?attr/colorControlNormal">
          <path
              android:fillType="evenOdd"
              android:fillColor="@android:color/white"
              android:pathData="M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2zM18.92,8h-2.95c-0.32,-1.25 -0.78,-2.45 -1.38,-3.56 1.84,0.63 3.37,1.91 4.33,3.56zM12,4.04c0.83,1.2 1.48,2.53 1.91,3.96h-3.82c0.43,-1.43 1.08,-2.76 1.91,-3.96zM4.26,14C4.1,13.36 4,12.69 4,12s0.1,-1.36 0.26,-2h3.38c-0.08,0.66 -0.14,1.32 -0.14,2 0,0.68 0.06,1.34 0.14,2L4.26,14zM5.08,16h2.95c0.32,1.25 0.78,2.45 1.38,3.56 -1.84,-0.63 -3.37,-1.9 -4.33,-3.56zM8.03,8L5.08,8c0.96,-1.66 2.49,-2.93 4.33,-3.56C8.81,5.55 8.35,6.75 8.03,8zM12,19.96c-0.83,-1.2 -1.48,-2.53 -1.91,-3.96h3.82c-0.43,1.43 -1.08,2.76 -1.91,3.96zM14.34,14L9.66,14c-0.09,-0.66 -0.16,-1.32 -0.16,-2 0,-0.68 0.07,-1.35 0.16,-2h4.68c0.09,0.65 0.16,1.32 0.16,2 0,0.68 -0.07,1.34 -0.16,2zM14.59,19.56c0.6,-1.11 1.06,-2.31 1.38,-3.56h2.95c-0.96,1.65 -2.49,2.93 -4.33,3.56zM16.36,14c0.08,-0.66 0.14,-1.32 0.14,-2 0,-0.68 -0.06,-1.34 -0.14,-2h3.38c0.16,0.64 0.26,1.31 0.26,2s-0.1,1.36 -0.26,2h-3.38z"/>
        </vector>
        """.trimIndent()
        assertEquals(
            expected = VectorDrawable(
                widthInDp = "24dp",
                heightInDp = "24dp",
                viewportWidth = 24f,
                viewportHeight = 24f,
                tint = "?attr/colorControlNormal",
                children = listOf(
                    VectorDrawable.Path(
                        fillType = "evenOdd",
                        fillColor = "@android:color/white",
                        pathData = "M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2zM18.92,8h-2.95c-0.32,-1.25 -0.78,-2.45 -1.38,-3.56 1.84,0.63 3.37,1.91 4.33,3.56zM12,4.04c0.83,1.2 1.48,2.53 1.91,3.96h-3.82c0.43,-1.43 1.08,-2.76 1.91,-3.96zM4.26,14C4.1,13.36 4,12.69 4,12s0.1,-1.36 0.26,-2h3.38c-0.08,0.66 -0.14,1.32 -0.14,2 0,0.68 0.06,1.34 0.14,2L4.26,14zM5.08,16h2.95c0.32,1.25 0.78,2.45 1.38,3.56 -1.84,-0.63 -3.37,-1.9 -4.33,-3.56zM8.03,8L5.08,8c0.96,-1.66 2.49,-2.93 4.33,-3.56C8.81,5.55 8.35,6.75 8.03,8zM12,19.96c-0.83,-1.2 -1.48,-2.53 -1.91,-3.96h3.82c-0.43,1.43 -1.08,2.76 -1.91,3.96zM14.34,14L9.66,14c-0.09,-0.66 -0.16,-1.32 -0.16,-2 0,-0.68 0.07,-1.35 0.16,-2h4.68c0.09,0.65 0.16,1.32 0.16,2 0,0.68 -0.07,1.34 -0.16,2zM14.59,19.56c0.6,-1.11 1.06,-2.31 1.38,-3.56h2.95c-0.96,1.65 -2.49,2.93 -4.33,3.56zM16.36,14c0.08,-0.66 0.14,-1.32 0.14,-2 0,-0.68 -0.06,-1.34 -0.14,-2h3.38c0.16,0.64 0.26,1.31 0.26,2s-0.1,1.36 -0.26,2h-3.38z",
                    ),
                ),
            ),
            actual = XmlDeserializer.deserialize(vector),
        )
    }

    @Test
    fun `serialize invalid XML returns Failure`() {
        val vector = """
        <note>
            <to>Tove</to>
            <from>Jani</from>
            <heading>Reminder</heading>
            <body>Don't forget me this weekend!</body>
        </note>
        """.trimIndent()
        assertFailsWith<XmlException> { XmlDeserializer.deserialize(vector) }
    }

    @Test
    fun `serialize XML with group`() {
        val vector = """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:height="24dp"
                android:width="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24">
               <group
                     android:name="rotationGroup"
                     android:pivotX="10.0"
                     android:pivotY="10.0"
                     android:rotation="15.0" >
                  <path
                    android:name="vect"
                    android:fillColor="#FF000000"
                    android:pathData="M15.67,4H14V2h-4v2H8.33C7.6,4 7,4.6 7,5.33V9h4.93L13,7v2h4V5.33C17,4.6 16.4,4 15.67,4z"
                    android:fillAlpha=".3"/>
                  <path
                    android:name="draw"
                    android:fillColor="#FF000000"
                    android:pathData="M13,12.5h2L11,20v-5.5H9L11.93,9H7v11.67C7,21.4 7.6,22 8.33,22h7.33c0.74,0 1.34,-0.6 1.34,-1.33V9h-4v3.5z"/>
               </group>
            </vector>
        """.trimIndent()
        assertEquals(
            expected = VectorDrawable(
                widthInDp = "24dp",
                heightInDp = "24dp",
                viewportWidth = 24f,
                viewportHeight = 24f,
                tint = null,
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
                                fillType = "nonZero",
                                alpha = .3f,
                            ),
                            VectorDrawable.Path(
                                name = "draw",
                                fillColor = "#FF000000",
                                pathData = "M13,12.5h2L11,20v-5.5H9L11.93,9H7v11.67C7,21.4 7.6,22 8.33,22h7.33c0.74,0 1.34,-0.6 1.34,-1.33V9h-4v3.5z",
                                fillType = "nonZero",
                            ),
                        ),
                    ),
                ),
            ),
            actual = XmlDeserializer.deserialize(vector),
        )
    }

    @Test
    fun `serialize XML with more dimensional groups`() {
        val vector = """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:height="24dp"
                android:width="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24">
               <group android:name="parent">
                  <group android:name="parent.first">
                    <path android:pathData="M15.67,4" />
                  </group>
                  <group android:name="parent.second">
                    <path android:pathData="M13,12.5"/>
                  </group>
               </group>
            </vector>
        """.trimIndent()
        assertEquals(
            expected = VectorDrawable(
                widthInDp = "24dp",
                heightInDp = "24dp",
                viewportWidth = 24f,
                viewportHeight = 24f,
                tint = null,
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
            ),
            actual = XmlDeserializer.deserialize(vector),
        )
    }
}
