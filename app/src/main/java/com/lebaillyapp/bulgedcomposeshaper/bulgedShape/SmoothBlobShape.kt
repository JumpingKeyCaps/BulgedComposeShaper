package com.lebaillyapp.bulgedcomposeshaper.bulgedShape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A custom [Shape] that generates an organic "blob" outline using
 * radial distortion and Catmull–Rom spline smoothing.
 *
 * The blob is defined by a list of radial fractions applied around
 * a circle. Each fraction scales the base radius at evenly spaced
 * angles around the center. These anchor points are then connected
 * smoothly with a Catmull–Rom spline, producing a fluid and
 * organic-looking outline.
 *
 * ### Parameters
 * @property radiiFractions List of scaling factors (`>0f`) applied to the base radius.
 * Each value corresponds to a point around the circle at angle `2π * i / n`.
 * - `1f` → exactly the base radius
 * - `>1f` → outward bump
 * - `<1f` → inward dip
 * @property marginFactor Global shrink factor (`0f..1f`) applied to the base radius
 * before distortion.
 * Use this to ensure the blob does not touch the edges of its bounding box.
 * Typical values:
 * - `1f` → blob may touch the border
 * - `<1f` → safe margin
 *
 * ### Notes
 * - The number of points equals `radiiFractions.size`.
 *   More points → higher complexity in the blob shape.
 * - The Catmull–Rom implementation uses fixed subdivisions (`steps = 5`)
 *   between each pair of points. Increasing steps yields smoother curves
 *   at the cost of more path segments.
 * - The blob is always centered within its bounding box.
 *
 * ### Example
 * ```kotlin
 * val shape = SmoothBlobShape(
 *     radiiFractions = listOf(1f, 0.9f, 1.2f, 0.8f, 1.1f, 0.95f),
 *     marginFactor = 0.85f
 * )
 * ```
 *
 * @see Shape
 * @see Outline.Generic
 */
class SmoothBlobShape(
    private val radiiFractions: List<Float>,
    private val marginFactor: Float = 0.85f // ≤ 1f pour buffer
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val cx = size.width / 2
        val cy = size.height / 2
        val baseRadius = size.minDimension / 2 * marginFactor // <-- réduit pour ne pas toucher les bords

        val n = radiiFractions.size
        val points = radiiFractions.mapIndexed { i, frac ->
            val theta = 2f * PI.toFloat() * i / n
            Offset(cx + baseRadius * frac * cos(theta), cy + baseRadius * frac * sin(theta))
        }

        // Catmull-Rom smoothing
        fun catmullRom(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset {
            val t2 = t * t
            val t3 = t2 * t
            val x = 0.5f * ((2f * p1.x) +
                    (-p0.x + p2.x) * t +
                    (2f*p0.x - 5f*p1.x + 4f*p2.x - p3.x) * t2 +
                    (-p0.x + 3f*p1.x - 3f*p2.x + p3.x) * t3)
            val y = 0.5f * ((2f * p1.y) +
                    (-p0.y + p2.y) * t +
                    (2f*p0.y - 5f*p1.y + 4f*p2.y - p3.y) * t2 +
                    (-p0.y + 3f*p1.y - 3f*p2.y + p3.y) * t3)
            return Offset(x, y)
        }

        val steps = 5
        path.moveTo(points[0].x, points[0].y)
        for (i in points.indices) {
            val p0 = points[(i - 1 + n) % n]
            val p1 = points[i]
            val p2 = points[(i + 1) % n]
            val p3 = points[(i + 2) % n]
            for (s in 1..steps) {
                val t = s / steps.toFloat()
                val pt = catmullRom(p0, p1, p2, p3, t)
                path.lineTo(pt.x, pt.y)
            }
        }
        path.close()
        return Outline.Generic(path)
    }
}