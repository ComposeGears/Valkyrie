package io.github.composegears.valkyrie.parser.svgxml.xml.ext

import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrPathNode
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.parser.svgxml.xml.PathParser
import org.xmlpull.v1.XmlPullParser

internal fun XmlPullParser.valueAsPathData(): List<IrPathNode> {
    return PathParser.parsePathString(getAttribute("android:pathData").orEmpty())
}

internal fun XmlPullParser.valueAsFillType(): IrPathFillType {
    return when (getAttribute("android:fillType")) {
        "evenOdd" -> IrPathFillType.EvenOdd
        else -> IrPathFillType.NonZero
    }
}

internal fun XmlPullParser.valueAsStrokeCap(): IrStrokeLineCap {
    val value = getAttribute("android:strokeLineCap")

    return IrStrokeLineCap.entries
        .find { it.name.equals(value, ignoreCase = true) }
        ?: IrStrokeLineCap.Butt
}

internal fun XmlPullParser.valueAsStrokeLineJoin(): IrStrokeLineJoin {
    val value = getAttribute("android:strokeLineJoin")

    return IrStrokeLineJoin.entries
        .find { it.name.equals(value, ignoreCase = true) }
        ?: IrStrokeLineJoin.Miter
}

internal fun XmlPullParser.valueAsIrColor(name: String): IrColor? {
    return getAttribute(name)?.let { IrColor(it) }
}
