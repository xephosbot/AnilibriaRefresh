package com.xbot.designsystem.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.withAnnotation

@OptIn(ExperimentalTextApi::class)
fun <R : Any> Builder.withRoundedCorner(block: Builder.() -> R) =
    withAnnotation(TAG, "ignored", block)

@Composable
fun rememberRoundedCornerSpan(
    cornerSize: CornerSize,
    background: Brush,
    stroke: BorderStroke?,
    padding: PaddingValues,
): RoundedCornerSpan {
    return remember {
        RoundedCornerSpan(cornerSize, background, stroke, padding)
    }
}

class RoundedCornerSpan(
    val cornerSize: CornerSize,
    val background: Brush,
    val stroke: BorderStroke?,
    val padding: PaddingValues,
) {
    private var textBounds: List<Rect> = emptyList()

    fun onDraw(scope: DrawScope) = with(scope) {
        for (bound in textBounds) {
            val rect = bound.copy(
                top = bound.top - padding.calculateTopPadding().toPx(),
                left = bound.left - padding.calculateLeftPadding(layoutDirection).toPx(),
                right = bound.right + padding.calculateRightPadding(layoutDirection).toPx(),
                bottom = bound.bottom + padding.calculateBottomPadding().toPx()
            )
            val cornerRadius = cornerSize.toPx(rect.size, this)
            drawRoundRect(
                brush = background,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Fill,
                cornerRadius = CornerRadius(cornerRadius)
            )
            stroke?.let { stroke ->
                drawRoundRect(
                    brush = stroke.brush,
                    topLeft = rect.topLeft,
                    size = rect.size,
                    style = Stroke(width = stroke.width.toPx()),
                    cornerRadius = CornerRadius(cornerRadius)
                )
            }
        }
    }

    fun onTextLayout(layoutResult: TextLayoutResult) {
        val text = layoutResult.layoutInput.text
        val annotation = text.getStringAnnotations(TAG, 0, text.length).first()
        textBounds = layoutResult.getBoundingBoxes(annotation.start, annotation.end)
    }
}

internal fun TextLayoutResult.getBoundingBoxes(startOffset: Int, endOffset: Int): List<Rect> {
    val startLineNum = getLineForOffset(startOffset)
    val endLineNum = getLineForOffset(endOffset)

    return (startLineNum..endLineNum).map { lineNum ->
        Rect(
            top = getLineTop(lineNum),
            bottom = getLineBottom(lineNum),
            left = if (lineNum == startLineNum) {
                getHorizontalPosition(startOffset, usePrimaryDirection = true)
            } else {
                getLineLeft(lineNum)
            },
            right = if (lineNum == endLineNum) {
                getHorizontalPosition(endOffset, usePrimaryDirection = true)
            } else {
                getLineRight(lineNum)
            }
        )
    }
}

private const val TAG = "rounded_corner"