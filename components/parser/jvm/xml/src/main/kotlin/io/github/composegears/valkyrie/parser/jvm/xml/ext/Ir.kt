package io.github.composegears.valkyrie.parser.jvm.xml.ext

import io.github.composegears.valkyrie.parser.common.AndroidColorParser
import io.github.composegears.valkyrie.parser.common.PathParser
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
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
    return getAttribute(name)?.let { value ->
        AndroidColorParser.parse(value) ?: IrColor(value).takeUnless { it.isTransparent() }
    }
}
