@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.theme

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.internal.rememberAnimatedShape
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon

interface ExpressiveShape {
    @Composable
    fun shapeForInteraction(pressed: Boolean, selected: Boolean): Shape
}

internal class MorphingExpressiveShape(
    private val shape: RoundedPolygon,
    private val pressedShape: RoundedPolygon,
    private val animationSpec: FiniteAnimationSpec<Float>
) : ExpressiveShape {

    @Composable
    override fun shapeForInteraction(pressed: Boolean, selected: Boolean): Shape {
        val morph = remember { Morph(shape, pressedShape) }
        val progress by animateFloatAsState(
            targetValue = if (pressed) 1f else 0f,
            animationSpec = animationSpec,
            label = "MorphingProgress"
        )
        return MorphPolygonShape(morph, progress)
    }
}

internal class RoundedCornerExpressiveShape(
    private val shape: Shape,
    private val pressedShape: Shape,
    private val selectedShape: Shape,
    private val animationSpec: FiniteAnimationSpec<Float>
) : ExpressiveShape {

    @Composable
    override fun shapeForInteraction(pressed: Boolean, selected: Boolean): Shape {
        val targetShape = if (selected) selectedShape else if (pressed) pressedShape else shape

        return if (targetShape is RoundedCornerShape) {
            rememberAnimatedShape(
                currentShape = targetShape,
                animationSpec = animationSpec
            )
        } else targetShape
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private class MorphPolygonShape(
    private val morph: Morph,
    private val percentage: Float
) : Shape {
    private val matrix = Matrix()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // Below assumes that you haven't changed the default radius of 1f, nor the centerX and centerY of 0f
        // By default this stretches the path to the size of the container, if you don't want stretching, use the same size.width for both x and y.
        matrix.scale(size.width, size.height)
        matrix.translate(0f, 0f)

        val path = morph.toPath(progress = percentage)
        path.transform(matrix)
        return Outline.Generic(path)
    }
}