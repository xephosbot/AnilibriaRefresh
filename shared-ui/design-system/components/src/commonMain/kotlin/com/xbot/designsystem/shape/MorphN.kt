/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.shape

import androidx.compose.ui.graphics.Path
import androidx.graphics.shapes.AngleEpsilon
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.LengthMeasurer
import androidx.graphics.shapes.MeasuredPolygon
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.featureMapper
import androidx.graphics.shapes.positiveModulo

/**
 * Multi-shape variant of [androidx.graphics.shapes.Morph], vendored from AOSP ahead of its public
 * release: interpolates between any number of [RoundedPolygon]s using one weight per shape.
 * Remove once `MorphN` ships in a public androidx.graphics.shapes build.
 */
class MorphN(private val shapes: List<RoundedPolygon>) {

    /**
     * The structure which holds the actual shape being morphed. It contains all cubics necessary
     * to represent the shapes (the original cubics in the shapes may be cut to align them),
     * matched one to one across all shapes in each inner list.
     */
    private val morphMatch: List<List<Cubic>> = match(shapes)

    /**
     * Returns a representation of the morph object at the given [progress] weights as a list of
     * Cubics. Note that this function causes a new list to be created and populated, so there is
     * some overhead.
     *
     * @param progress one weight per shape, in the order the shapes were provided at construction
     *   time. The cubics are the weighted average of the matched cubics, normalized by the sum of
     *   the weights, so the weights only need to be non-negative and sum to a positive value.
     */
    fun asCubics(progress: List<Float>): List<Cubic> {
        return buildList {
            // The first/last mechanism here ensures that the final anchor point in the shape
            // exactly matches the first anchor point. There can be rendering artifacts introduced
            // by those points being slightly off, even by much less than a pixel
            var firstCubic: Cubic? = null
            var lastCubic: Cubic? = null
            val sp = progress.sum()
            require(progress.size == shapes.size)
            require(sp > 0f)

            for (i in morphMatch.indices) {
                val cubic =
                    Cubic(
                        FloatArray(8) {
                            morphMatch[i]
                                .mapIndexed { shapeIx, cubic ->
                                    cubic.points[it] * progress[shapeIx]
                                }
                                .sum() / sp
                        }
                    )
                if (firstCubic == null) firstCubic = cubic
                if (lastCubic != null) add(lastCubic)
                lastCubic = cubic
            }
            if (lastCubic != null && firstCubic != null)
                add(
                    Cubic(
                        lastCubic.anchor0X,
                        lastCubic.anchor0Y,
                        lastCubic.control0X,
                        lastCubic.control0Y,
                        lastCubic.control1X,
                        lastCubic.control1Y,
                        firstCubic.anchor0X,
                        firstCubic.anchor0Y
                    )
                )
        }
    }

    private companion object {
        /**
         * [match], called at MorphN construction time, creates the structure used to animate
         * between the shapes. The technique is to match geometry (curves) between the shapes when
         * and where possible, and to create new/placeholder curves when necessary (when one of the
         * shapes has more curves than the other). The result is a list of matched curve groups:
         * each group holds one cubic per shape, and changing the progress of a MorphN object
         * simply takes the weighted average within all groups.
         */
        fun match(ps: List<RoundedPolygon>): List<List<Cubic>> {
            val n = ps.size

            // Measure polygons, returns lists of measured cubics for each polygon, which
            // we then use to match the curves
            val measuredPolygons =
                ps.map { p -> MeasuredPolygon.measurePolygon(LengthMeasurer(), p) }

            // features will contain the list of corners (just the inner circular curve)
            // along with the progress at the middle of those corners. These measurement values
            // are then used to compare and match between the polygons
            val features = measuredPolygons.map { it.features }

            // Map features: doubleMapper is the result of mapping the features in each shape to
            // the closest feature in the other shape.
            // Given a progress in one of the shapes it can be used to find the corresponding
            // progress in the other shape (in both directions).
            // Note that we match the first polygon against all others.
            val doubleMappers = (1 until n).map { featureMapper(features[0], features[it]) }

            // cut points on polygons 2+ is the mapping of the 0 point on the first polygon
            val polygonCutPoints = doubleMappers.map { it.map(0f) }

            // Cut and rotate.
            // Polygons start at progress 0, and the featureMapper has decided that we want to
            // match progress 0 in the first polygon to `polygonCutPoints` on the other polygons.
            // So we need to cut the other polygons there and "rotate" them, so as we walk through
            // all polygons we can find the matching.
            // The resulting 'bss' are MeasuredPolygons, whose MeasuredCubics start from
            // outlineProgress=0 and increase until outlineProgress=1
            val bss = buildList {
                add(measuredPolygons[0])
                polygonCutPoints.forEachIndexed { ix, cutPoint ->
                    add(measuredPolygons[ix + 1].cutAndShift(cutPoint))
                }
            }

            // Match
            // Now we can compare the lists of measured cubics and create a list of groups of
            // cubics [ret], which are the curves that represent the MorphN object and the source
            // shapes, and which can be interpolated to animate between those shapes.
            val ret = mutableListOf<List<Cubic>>()

            // These are the indices of the current cubic on each of the polygons
            val indices = Array(n) { 1 }

            // beziers are the current measured cubic for each polygon
            val beziers = Array(n) { bss[it].getOrNull(0) }

            // Iterate until all curves are accounted for and matched
            while (beziers.all { it != null }) {
                // Progresses are in shape1's perspective
                // 'ends' are ending progress values of current measured cubics in [0,1] range
                val ends =
                    (0 until n).map {
                        if (indices[it] == bss[it].size) 1f
                        else {
                            val p = beziers[it]!!.endOutlineProgress
                            if (it == 0) p
                            else
                                doubleMappers[it - 1].mapBack(
                                    positiveModulo(p + polygonCutPoints[it - 1], 1f)
                                )
                        }
                    }
                val minEnd = ends.min()

                // minEnd is the progress at which the curve that ends first ends.
                // If all curves end roughly there, no cutting is needed, we have a match.
                // If one curve extends beyond, we need to cut it.
                val addToRet = mutableListOf<Cubic>()

                for (ix in 0 until n) {
                    if (ends[ix] > minEnd + AngleEpsilon) {
                        val cutPoint =
                            if (ix == 0) minEnd
                            else
                                positiveModulo(
                                    doubleMappers[ix - 1].map(minEnd) - polygonCutPoints[ix - 1],
                                    1f
                                )

                        val (b1, b2) = beziers[ix]!!.cutAtProgress(cutPoint)
                        addToRet.add(b1.cubic)
                        beziers[ix] = b2
                    } else {
                        addToRet.add(beziers[ix]!!.cubic)
                        beziers[ix] = bss[ix].getOrNull(indices[ix]++)
                    }
                }
                ret.add(addToRet)
            }
            require(beziers.all { it == null }) {
                "Expected all Polygon's Cubic to be fully matched"
            }
            return ret
        }
    }
}

/**
 * Generates a Compose [Path] from the morph at the given [progress] weights.
 *
 * @param progress one weight per morphed shape, see [MorphN.asCubics].
 * @param path an optional [Path] object to rewind and reuse, to avoid per-frame allocation.
 */
internal fun MorphN.toPath(progress: List<Float>, path: Path = Path()): Path {
    path.rewind()
    var first = true
    val cubics = asCubics(progress)
    for (i in cubics.indices) {
        val cubic = cubics[i]
        if (first) {
            path.moveTo(cubic.anchor0X, cubic.anchor0Y)
            first = false
        }
        path.cubicTo(
            cubic.control0X,
            cubic.control0Y,
            cubic.control1X,
            cubic.control1Y,
            cubic.anchor1X,
            cubic.anchor1Y,
        )
    }
    path.close()
    return path
}
