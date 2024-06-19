package androidx.compose.material.icons.generator.vector

import androidx.compose.material.icons.generator.MemberNames
import com.squareup.kotlinpoet.MemberName

enum class StrokeJoin(val memberName: MemberName) {
    Miter(MemberNames.StrokeJoinMiter),
    Round(MemberNames.StrokeJoinRound),
    Bevel(MemberNames.StrokeJoinBevel)
}