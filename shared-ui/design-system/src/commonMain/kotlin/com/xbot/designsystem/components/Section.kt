package com.xbot.designsystem.components

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.times

data class SectionShape(
    val innerCornerRadius: Dp,
    val outerCornerRadius: Dp,
)

data class SectionSpacing(
    val innerSpacing: Dp,
    val outerSpacing: Dp,
)

object SectionDefaults {

    private val DefaultInnerCornerRadius = 4.dp
    private val DefaultOuterCornerRadius = 16.dp

    private val DefaultInnerSpacing = 2.dp
    private val DefaultOuterSpacing = 16.dp

    fun shape(
        innerCornerRadius: Dp = DefaultInnerCornerRadius,
        outerCornerRadius: Dp = DefaultOuterCornerRadius,
    ): SectionShape = SectionShape(
        innerCornerRadius = innerCornerRadius,
        outerCornerRadius = outerCornerRadius,
    )

    fun spacing(
        innerSpacing: Dp = DefaultInnerSpacing,
        outerSpacing: Dp = DefaultOuterSpacing,
    ): SectionSpacing = SectionSpacing(
        innerSpacing = innerSpacing,
        outerSpacing = outerSpacing,
    )

    internal fun itemShape(
        index: Int,
        itemsCount: Int,
        columnsCount: Int,
        sectionShape: SectionShape,
    ): Shape {
        @Suppress("NAME_SHADOWING")
        val columnsCount = if (columnsCount != 0) columnsCount else 1

        val inner = CornerSize(sectionShape.innerCornerRadius)
        val outer = CornerSize(sectionShape.outerCornerRadius)

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
    sectionShape: SectionShape = SectionDefaults.shape(),
    sectionSpacing: SectionSpacing = SectionDefaults.spacing(),
): Modifier =
    sectionSpacing(
        index = index,
        itemsCount = itemsCount,
        columnsCount = columnsCount,
        sectionSpacing = sectionSpacing
    ).sectionShape(
        index = index,
        itemsCount = itemsCount,
        columnsCount = columnsCount,
        sectionShape = sectionShape
    )

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

    val paddingPerItem =
        (2 * sectionSpacing.outerSpacing + (columnsCount - 1) * sectionSpacing.innerSpacing) / columnsCount
    val translation = if (columnIndex == 0) {
        sectionSpacing.outerSpacing
    } else {
        sectionSpacing.outerSpacing + columnIndex * sectionSpacing.innerSpacing - columnIndex * paddingPerItem
    }

    return this
        .layout { measurable, constraints ->
            val endPadding = paddingPerItem.roundToPx()
            val adjustedConstraints = constraints.offset(horizontal = -endPadding)
            val placeable = measurable.measure(adjustedConstraints)
            val spacing = if (!isBottom) sectionSpacing.innerSpacing.roundToPx() else 0
            layout(placeable.width + endPadding, placeable.height + spacing) {
                placeable.placeWithLayer(0, 0) {
                    translationX = translation.toPx()
                }
            }
        }
}
