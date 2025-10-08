package io.github.composegears.valkyrie.completion

import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.openapi.diagnostic.Logger
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
    private var cachedImage: Image? = null

    override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
        if (g !is Graphics2D) return

        if (cachedImage == null) {
            cachedImage = renderImage()
        }
        val img = cachedImage ?: return
        g.drawImage(img, x, y, size, size, null)
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

    companion object {
        private const val ICON_SCALE_FACTOR = 4
    }
}
