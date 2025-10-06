package io.github.composegears.valkyrie.completion

import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.scale.JBUIScale
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import javax.swing.Icon

class ImageVectorIcon(
    private val vectorXml: String,
    private val size: Int = 16,
) : Icon {

    @Volatile
    private var lastScale: Float = -1f

    @Volatile
    private var cachedImage: Image? = null

    override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
        if (g !is Graphics2D) return
        val scale = JBUIScale.sysScale(g)

        if (cachedImage == null || scale != lastScale) {
            cachedImage = renderImage(scale)
            lastScale = scale
        }
        val img = cachedImage ?: return
        g.drawImage(img, x, y, size, size, null)
    }

    private fun renderImage(scale: Float): Image? {
        val maxDimension = (size * scale).toInt().coerceAtLeast(1)
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
}
