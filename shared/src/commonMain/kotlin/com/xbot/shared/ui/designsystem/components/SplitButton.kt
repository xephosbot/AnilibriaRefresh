@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.shared.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LeadingButtonLayoutId
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.TrailingButtonLayoutId
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastMaxOfOrNull
import androidx.compose.ui.util.fastSumBy

@ExperimentalMaterial3ExpressiveApi
@Composable
fun SplitButtonLayout(
    leadingButton: @Composable () -> Unit,
    trailingButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    spacing: Dp = SplitButtonDefaults.Spacing,
) {
    Layout(
        {
            // Override min component size enforcement to avoid create extra padding internally
            // Enforce it on the parent instead
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                Box(
                    modifier = Modifier.layoutId(LeadingButtonLayoutId),
                    contentAlignment = Alignment.Center,
                    content = { leadingButton() }
                )
                Box(
                    modifier = Modifier.layoutId(TrailingButtonLayoutId),
                    contentAlignment = Alignment.Center,
                    content = { trailingButton() }
                )
            }
        },
        modifier.minimumInteractiveComponentSize(),
        measurePolicy = { measurables, constraints ->
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            val trailingWidth = measurables
                .fastFirst { it.layoutId == TrailingButtonLayoutId }
                .maxIntrinsicWidth(0)

            val leadingButtonPlaceable =
                measurables
                    .fastFirst { it.layoutId == LeadingButtonLayoutId }
                    .measure(
                        looseConstraints.copy(
                            minWidth = (constraints.maxWidth - trailingWidth).coerceAtLeast(0),
                            maxWidth = (constraints.maxWidth - trailingWidth).coerceAtLeast(0)
                        )
                    )

            val trailingButtonPlaceable =
                measurables
                    .fastFirst { it.layoutId == TrailingButtonLayoutId }
                    .measure(
                        looseConstraints
                            .offset(
                                horizontal = -(leadingButtonPlaceable.width + spacing.roundToPx())
                            )
                            .copy(
                                minHeight = leadingButtonPlaceable.height,
                                maxHeight = leadingButtonPlaceable.height
                            )
                    )

            val placeables = listOf(leadingButtonPlaceable, trailingButtonPlaceable)

            val contentWidth = placeables.fastSumBy { it.width } + spacing.roundToPx()
            val contentHeight = placeables.fastMaxOfOrNull { it.height } ?: 0

            val width = constraints.constrainWidth(contentWidth)
            val height = constraints.constrainHeight(contentHeight)

            layout(width, height) {
                leadingButtonPlaceable.placeRelative(0, 0)
                trailingButtonPlaceable.placeRelative(
                    x = leadingButtonPlaceable.width + spacing.roundToPx(),
                    y = 0
                )
            }
        }
    )
}