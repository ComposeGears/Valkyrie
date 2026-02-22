package io.github.composegears.valkyrie.completion

import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.JBColor
import io.github.composegears.valkyrie.sdk.ir.util.internal.DominantShade
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.io.Serializable
import javax.swing.Icon

class ImageVectorIcon(
    private val vectorXml: String,
    private val size: Int = 16,
    private val aspectRatio: Float,
    private val dominantShade: DominantShade,
) : Icon,
    Serializable {

    @Volatile
    @Transient
    private var cachedImage: Image? = null

    override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
        if (g !is Graphics2D) return

        // Validate aspect ratio before proceeding
        if (!aspectRatio.isFinite()) return

        drawBackground(g = g, x = x, y = y)

        if (cachedImage == null) {
            cachedImage = renderImage()
        }
        val img = cachedImage ?: return

        // Calculate dimensions preserving aspect ratio
        val (width, height) = when {
            aspectRatio > 1f -> size to (size / aspectRatio).toInt()
            else -> (size * aspectRatio).toInt() to size
        }

        // Center the icon if it's smaller than the allocated space
        val offsetX = x + (size - width) / 2
        val offsetY = y + (size - height) / 2

        g.drawImage(img, offsetX, offsetY, width, height, null)
    }

    private fun drawBackground(g: Graphics2D, x: Int, y: Int) {
        when (dominantShade) {
            DominantShade.Black -> {
                g.color = blackIconBackgroundColor
                g.fillRoundRect(x, y, size, size, 2, 2)
            }
            DominantShade.White -> {
                g.color = whiteIconBackgroundColor
                g.fillRoundRect(x, y, size, size, 2, 2)
            }
            DominantShade.Mixed -> {}
        }
    }

    private fun renderImage(): Image? {
        val maxDimension = ICON_SCALE_FACTOR * size
        val errorLog = StringBuilder()

        val imageBuffer = VdPreview.getPreviewFromVectorXml(
            VdPreview.TargetSize.createFromMaxDimension(maxDimension),
            vectorXml,
            errorLog,
        )

        if (errorLog.isNotEmpty()) {
            Logger.getInstance(ImageVectorIcon::class.java)
                .error("Failed to create icon preview: $errorLog")
        }

        return imageBuffer
    }

    override fun getIconWidth(): Int = size
    override fun getIconHeight(): Int = size

    override fun toString(): String {
        return "ImageVectorIcon(size=$size, aspectRatio=$aspectRatio, dominantShade=$dominantShade, vectorXml=${vectorXml.take(100)})"
    }

    companion object {
        private const val ICON_SCALE_FACTOR = 4

        private val blackIconBackgroundColor = JBColor(Color(0, 0, 0, 0), Color.WHITE)
        private val whiteIconBackgroundColor = JBColor(Color.DARK_GRAY, Color(0, 0, 0, 0))
    }
}
