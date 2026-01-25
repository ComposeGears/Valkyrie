package io.github.composegears.valkyrie.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow

@Composable
fun InfoCard(
    onClick: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    image: ImageVector? = null,
    icon: ImageVector? = null,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        CenterVerticalRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            icon?.let {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .size(36.dp),
                    imageVector = it,
                    contentDescription = null,
                )
            }
            image?.let {
                Image(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .size(36.dp),
                    imageVector = it,
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier.width(250.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                    maxLines = 2,
                )
            }
        }
    }
}
