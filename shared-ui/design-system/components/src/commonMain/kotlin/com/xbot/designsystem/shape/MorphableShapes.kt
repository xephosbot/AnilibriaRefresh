@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.shape

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.internal.rememberAnimatedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.RoundedPolygon

/**
 * A group of [Shape]s that can morph between different interaction states.
 *
 * Mirrors the upcoming Material3 expressive `MorphableShapes` API, so once it ships in a public
 * release call sites only need to swap the import.
 */
interface MorphableShapes {

    /**
     * Resolves the target shape for the given interaction states, in priority order:
     * pressed > checked > dragged > selected > focused > hovered.
     */
    fun resolveShape(
        pressed: Boolean = false,
        checked: Boolean = false,
        selected: Boolean = false,
        hovered: Boolean = false,
        focused: Boolean = false,
        dragged: Boolean = false,
    ): Shape
}

/** Creates a [MorphableShapes] that resolves between per-interaction-state [Shape]s. */
fun MorphableShapes(
    shape: Shape,
    pressedShape: Shape = shape,
    checkedShape: Shape = shape,
    selectedShape: Shape = shape,
    hoveredShape: Shape = shape,
    focusedShape: Shape = shape,
    draggedShape: Shape = shape,
): MorphableShapes = ShapesByInteraction(
    shape = shape,
    pressedShape = pressedShape,
    checkedShape = checkedShape,
    selectedShape = selectedShape,
    hoveredShape = hoveredShape,
    focusedShape = focusedShape,
    draggedShape = draggedShape,
)

/**
 * Creates a [MorphableShapes] that morphs between per-interaction-state [RoundedPolygon]s. States
 * sharing the same polygon instance share a weight in the underlying [MorphN], so the common
 * two-state case morphs between just two polygons. The polygons are expected to be normalized to
 * the unit square, which is the case for all `MaterialShapes` values.
 */
fun MorphableShapes(
    shape: RoundedPolygon,
    pressedShape: RoundedPolygon = shape,
    checkedShape: RoundedPolygon = shape,
    selectedShape: RoundedPolygon = shape,
    hoveredShape: RoundedPolygon = shape,
    focusedShape: RoundedPolygon = shape,
    draggedShape: RoundedPolygon = shape,
): MorphableShapes = PolygonShapes(
    shape = shape,
    pressedShape = pressedShape,
    checkedShape = checkedShape,
    selectedShape = selectedShape,
    hoveredShape = hoveredShape,
    focusedShape = focusedShape,
    draggedShape = draggedShape,
)

/**
 * Resolves and remembers a [Shape] that smoothly morphs between the shapes provided by [shapes]
 * based on the interaction states.
 *
 * Corner-based shapes animate their corner sizes with [animationSpec], polygon-based shapes morph
 * their geometry. Anything else snaps to the target shape.
 */
@Composable
fun rememberMorphableShape(
    shapes: MorphableShapes,
    animationSpec: FiniteAnimationSpec<Float>,
    pressed: Boolean = false,
    checked: Boolean = false,
    selected: Boolean = false,
    hovered: Boolean = false,
    focused: Boolean = false,
    dragged: Boolean = false,
): Shape = when (shapes) {
    is PolygonShapes -> {
        val targetIndex = shapes.resolveIndex(pressed, checked, selected, hovered, focused, dragged)
        // One animated weight per distinct polygon. MorphN normalizes by the weight sum, so with
        // two states this reproduces plain two-shape morph progress, and a transition between any
        // two non-resting states morphs directly without passing through the base shape.
        val weights = List(shapes.polygons.size) { index ->
            animateFloatAsState(
                targetValue = if (index == targetIndex) 1f else 0f,
                animationSpec = animationSpec,
                label = "MorphableShapeWeight"
            )
        }
        // The weights are read inside createOutline, so the animation only invalidates the
        // layer instead of recomposing on every frame.
        remember(shapes) { MorphNShape(shapes.morph) { weights.map { it.value } } }
    }
    else -> {
        val targetShape = shapes.resolveShape(pressed, checked, selected, hovered, focused, dragged)
        if (targetShape is CornerBasedShape) {
            key(shapes) {
                rememberAnimatedShape(
                    currentShape = targetShape,
                    animationSpec = animationSpec,
                )
            }
        } else {
            targetShape
        }
    }
}

private data class ShapesByInteraction(
    val shape: Shape,
    val pressedShape: Shape,
    val checkedShape: Shape,
    val selectedShape: Shape,
    val hoveredShape: Shape,
    val focusedShape: Shape,
    val draggedShape: Shape,
) : MorphableShapes {

    override fun resolveShape(
        pressed: Boolean,
        checked: Boolean,
        selected: Boolean,
        hovered: Boolean,
        focused: Boolean,
        dragged: Boolean,
    ): Shape = when {
        pressed -> pressedShape
        checked -> checkedShape
        dragged -> draggedShape
        selected -> selectedShape
        focused -> focusedShape
        hovered -> hoveredShape
        else -> shape
    }
}

private data class PolygonShapes(
    val shape: RoundedPolygon,
    val pressedShape: RoundedPolygon,
    val checkedShape: RoundedPolygon,
    val selectedShape: RoundedPolygon,
    val hoveredShape: RoundedPolygon,
    val focusedShape: RoundedPolygon,
    val draggedShape: RoundedPolygon,
) : MorphableShapes {

    // The distinct polygons participating in the morph; states pointing at the same instance
    // share a weight.
    val polygons: List<RoundedPolygon> = listOf(
        shape, pressedShape, checkedShape, selectedShape, hoveredShape, focusedShape, draggedShape
    ).distinct()

    // Built lazily and reused: matching the polygons is the expensive part of a morph.
    val morph: MorphN by lazy { MorphN(polygons) }

    fun resolveIndex(
        pressed: Boolean,
        checked: Boolean,
        selected: Boolean,
        hovered: Boolean,
        focused: Boolean,
        dragged: Boolean,
    ): Int = polygons.indexOf(
        when {
            pressed -> pressedShape
            checked -> checkedShape
            dragged -> draggedShape
            selected -> selectedShape
            focused -> focusedShape
            hovered -> hoveredShape
            else -> shape
        }
    )

    override fun resolveShape(
        pressed: Boolean,
        checked: Boolean,
        selected: Boolean,
        hovered: Boolean,
        focused: Boolean,
        dragged: Boolean,
    ): Shape {
        val index = resolveIndex(pressed, checked, selected, hovered, focused, dragged)
        val weights = List(polygons.size) { if (it == index) 1f else 0f }
        return MorphNShape(morph) { weights }
    }
}

private class MorphNShape(
    private val morph: MorphN,
    private val weights: () -> List<Float>,
) : Shape {
    private val matrix = Matrix()
    private val path = Path()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // The polygons are normalized to the unit square, so scaling by the container size
        // stretches the path to fill the bounds. The matrix is reused across frames and has to
        // be reset before applying the scale again.
        matrix.reset()
        matrix.scale(size.width, size.height)
        morph.toPath(weights(), path)
        path.transform(matrix)
        return Outline.Generic(path)
    }
}
