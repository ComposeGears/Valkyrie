package androidx.compose.material.icons.generator.vector

import androidx.compose.material.icons.generator.MemberNames
import com.squareup.kotlinpoet.MemberName

enum class StrokeCap(val memberName: MemberName) {
    Butt(MemberNames.StrokeCapButt),
    Round(MemberNames.StrokeCapRound),
    Square(MemberNames.StrokeCapSquare)
}
