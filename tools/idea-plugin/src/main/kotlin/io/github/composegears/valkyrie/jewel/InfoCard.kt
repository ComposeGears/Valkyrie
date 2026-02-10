package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.core.FocusContainer
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.sdk.compose.icons.colored.GoogleMaterialLogo
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.BatchProcessing
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.typography

@Composable
fun InfoCard(
    onClick: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    key: IconKey? = null,
    tint: Color = JewelTheme.contentColor,
    iconSize: Dp = 36.dp,
) {
    FocusContainer(modifier = modifier, onClick = onClick) {
        Column {
            CenterVerticalRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                icon?.let {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .size(iconSize),
                        imageVector = it,
                        contentDescription = null,
                        tint = tint,
                    )
                }
                key?.let {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .size(iconSize),
                        key = it,
                        contentDescription = null,
                    )
                }
                Column(
                    modifier = Modifier.width(250.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = title,
                        style = JewelTheme.typography.h4TextStyle,
                        maxLines = 1,
                    )
                    InfoText(
                        text = description,
                        maxLines = 2,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun InfoCardPreview() = PreviewTheme(alignment = Alignment.Center) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        InfoCard(
            onClick = {},
            icon = ValkyrieIcons.Outlined.BatchProcessing,
            title = "Title",
            description = "Long description\nwith multiple lines",
        )
        InfoCard(
            onClick = {},
            icon = ValkyrieIcons.Colored.GoogleMaterialLogo,
            title = "Title",
            description = "Description",
            tint = Color.Unspecified,
        )
    }
}
