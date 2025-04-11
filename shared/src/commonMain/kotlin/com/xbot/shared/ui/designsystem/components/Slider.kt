package com.xbot.shared.ui.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import com.xbot.shared.ui.localization.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSlider(
    modifier: Modifier = Modifier,
    sliderPosition: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int? = 10
) {
    val calculatedSteps = steps ?: ((valueRange.start - valueRange.endInclusive) - 1).roundToInt()
        .coerceAtLeast(0)
    val startInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = valueRange.start.roundToInt().toString(),
            modifier = Modifier.semantics { contentDescription = "Minimum Value" },
        )

        Spacer(Modifier.width(8.dp))

        RangeSlider(
            modifier = Modifier.weight(1f),
            value = sliderPosition,
            steps = calculatedSteps,
            onValueChange = onValueChange,
            valueRange = valueRange,
            startInteractionSource = startInteractionSource,
            endInteractionSource = endInteractionSource,
            startThumb = {
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier
                                .sizeIn(45.dp, 25.dp)
                                .wrapContentWidth()
                        ) {
                            Text("%.0f".format(sliderPosition.start))
                        }
                    },
                    interactionSource = startInteractionSource,
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = startInteractionSource,
                    )
                }
            },
            endThumb = {
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier
                                .sizeIn(45.dp, 25.dp)
                                .wrapContentWidth(),
                        ) {
                            Text("%.0f".format(sliderPosition.endInclusive))
                        }
                    },
                    interactionSource = endInteractionSource,
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = endInteractionSource,
                    )
                }
            },
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = valueRange.endInclusive.roundToInt().toString(),
            modifier = Modifier.semantics { contentDescription = "Maximum Value" },
        )
    }
}