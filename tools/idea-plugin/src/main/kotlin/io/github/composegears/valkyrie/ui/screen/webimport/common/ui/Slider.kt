package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoStopIndicatorSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    modifier: Modifier = Modifier,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = SliderDefaults.colors().copy(
        inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
    )

    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        colors = colors,
        valueRange = valueRange,
        steps = steps,
        track = { sliderState ->
            SliderDefaults.Track(
                colors = colors,
                enabled = true,
                sliderState = sliderState,
                drawStopIndicator = null,
                drawTick = { _, _ -> },
            )
        },
    )
}
