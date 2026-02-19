package com.xbot.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.times
import com.xbot.designsystem.theme.LocalMargins

data class SectionShape(
    val innerCornerSize: CornerSize,
    val outerCornerSize: CornerSize,
)

data class SectionSpacing(
    val innerSpacing: Dp,
    val outerSpacing: PaddingValues,
)

object SectionDefaults {

    private val DefaultInnerSpacing = 2.dp

    @Composable
    fun shape(
        innerCornerSize: CornerSize = MaterialTheme.shapes.extraSmall.topStart,
        outerCornerSize: CornerSize = MaterialTheme.shapes.large.topStart,
    ): SectionShape = SectionShape(
        innerCornerSize = innerCornerSize,
        outerCornerSize = outerCornerSize,
    )

    @Composable
    fun spacing(
        innerSpacing: Dp = DefaultInnerSpacing,
        contentPadding: PaddingValues? = null,
    ): SectionSpacing {
        val margin = LocalMargins.current.horizontal
        
        val outerSpacing = remember(contentPadding, margin) {
            if (contentPadding != null) {
                object : PaddingValues {
                    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
                        contentPadding.calculateLeftPadding(layoutDirection) + margin

                    override fun calculateTopPadding(): Dp = 0.dp

                    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
                        contentPadding.calculateRightPadding(layoutDirection) + margin

                    override fun calculateBottomPadding(): Dp = 0.dp
                }
            } else {
                PaddingValues(horizontal = margin)
            }
        }

        return SectionSpacing(
            innerSpacing = innerSpacing,
            outerSpacing = outerSpacing,
        )
    }

    internal fun itemShape(
        index: Int,
        itemsCount: Int,
        columnsCount: Int,
        sectionShape: SectionShape,
    ): Shape {
        @Suppress("NAME_SHADOWING")
        val columnsCount = if (columnsCount != 0) columnsCount else 1

        val inner = sectionShape.innerCornerSize
        val outer = sectionShape.outerCornerSize

        if (itemsCount == 1) return RoundedCornerShape(outer)

        val isTop = index < columnsCount
        val isBottom = index >= itemsCount - columnsCount
        val isLeft = index % columnsCount == 0
        val isRight = (index + 1) % columnsCount == 0 || index == itemsCount - 1

        return RoundedCornerShape(
            topStart = if (isTop && isLeft) outer else inner,
            topEnd = if (isTop && isRight) outer else inner,
            bottomStart = if (isBottom && isLeft) outer else inner,
            bottomEnd = if (isBottom && isRight) outer else inner
        )
    }
}

fun Modifier.section(
    index: Int,
    itemsCount: Int,
    columnsCount: Int = 1,
    sectionShape: SectionShape? = null,
    sectionSpacing: SectionSpacing? = null,
): Modifier = composed {
    val resolvedShape = sectionShape ?: SectionDefaults.shape()
    val resolvedSpacing = sectionSpacing ?: SectionDefaults.spacing()

    sectionSpacing(
        index = index,
        itemsCount = itemsCount,
        columnsCount = columnsCount,
        sectionSpacing = resolvedSpacing
    ).sectionShape(
        index = index,
        itemsCount = itemsCount,
        columnsCount = columnsCount,
        sectionShape = resolvedShape
    )
}

private fun Modifier.sectionShape(
    index: Int,
    itemsCount: Int,
    columnsCount: Int,
    sectionShape: SectionShape,
): Modifier = clip(
    shape = SectionDefaults.itemShape(
        index = index,
        itemsCount = itemsCount,
        columnsCount = columnsCount,
        sectionShape = sectionShape,
    )
)

private fun Modifier.sectionSpacing(
    index: Int,
    itemsCount: Int,
    columnsCount: Int,
    sectionSpacing: SectionSpacing,
): Modifier {
    @Suppress("NAME_SHADOWING")
    val columnsCount = if (columnsCount != 0) columnsCount else 1

    val isBottom = index >= itemsCount - columnsCount
    val columnIndex = index % columnsCount

    return this
        .layout { measurable, constraints ->
            val startPadding = sectionSpacing.outerSpacing.calculateStartPadding(layoutDirection)
            val endPadding = sectionSpacing.outerSpacing.calculateEndPadding(layoutDirection)
            
            val paddingPerItem =
                (startPadding + endPadding + (columnsCount - 1) * sectionSpacing.innerSpacing) / columnsCount
            
            val translation = if (columnIndex == 0) {
                startPadding
            } else {
                startPadding + columnIndex * sectionSpacing.innerSpacing - columnIndex * paddingPerItem
            }
            
            val endPaddingPx = paddingPerItem.roundToPx()
            val adjustedConstraints = constraints.offset(horizontal = -endPaddingPx)
            val placeable = measurable.measure(adjustedConstraints)
            val spacing = if (!isBottom) sectionSpacing.innerSpacing.roundToPx() else 0
            layout(placeable.width + endPaddingPx, placeable.height + spacing) {
                placeable.placeWithLayer(0, 0) {
                    translationX = translation.toPx()
                }
            }
        }
}
