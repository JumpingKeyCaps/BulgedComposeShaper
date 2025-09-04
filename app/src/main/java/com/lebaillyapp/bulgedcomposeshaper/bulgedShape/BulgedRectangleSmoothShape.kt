package com.lebaillyapp.bulgedcomposeshaper.bulgedShape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * A custom [Shape] that draws a rectangle with uniform rounded corners
 * and optionally bulging edges.
 *
 * Unlike [BulgedRectangleFullShape], this variant uses a single corner radius
 * and a single bulge factor applied uniformly on all edges.
 *
 * The outline is constructed with cubic Bézier curves:
 * - Corners use a smoothing factor (`0f..1f`) to control how round/sharp they appear
 * - Edges can bulge outward (positive values) or inward (negative values)
 *
 * Bulges are distributed along edges using a sine wave, scaled by the smallest
 * dimension of the shape.
 *
 * ### Parameters
 * @property cornerRadius Uniform corner radius in [Dp].
 * Clamped so that it never exceeds half of the shortest side.
 * @property bulgeAmount Global bulge intensity applied to all edges.
 * - `0f` → flat edges
 * - `>0f` → outward bumps
 * - `<0f` → inward dips
 * The maximum displacement is proportional to half of the smallest dimension.
 * @property cornerSmoothFactor Proportion (`0f..1f`) of the corner radius used to "pull" the tangent.
 * - `0f` → sharp circular arc
 * - `1f` → wide and soft curve
 *
 * ### Notes
 * - The constant `C ≈ 0.55228` is used for circle approximation with Bézier curves.
 * - Bulges are symmetric on each edge (`sin` distribution).
 * - Best used for playful cards, blobs, or dynamic buttons when you want
 *   a simpler alternative to [BulgedRectangleFullShape].
 *
 * @see Shape
 * @see Outline.Generic
 */
class BulgedRectangleSmoothShape(
    private val cornerRadius: Dp,
    val bulgeAmount: Float = 0f,      // 0 = flat, positive = outward, negative = cave
    val cornerSmoothFactor: Float = 0.3f // proportion du rayon du coin pour "tirer" la tangente
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val width = size.width
        val height = size.height
        val cornerPx = with(density) { cornerRadius.toPx() }
        val radius = cornerPx.coerceAtMost(minOf(width, height) / 2f)

        val maxBulgePx = min(width, height) * bulgeAmount * 0.5f
        val C = 0.5522847498f

        fun bulgeAtEdge(t: Float): Float = maxBulgePx * sin(t * PI).toFloat()

        val left = 0f
        val top = 0f
        val right = width
        val bottom = height
        val xRadius = radius
        val yRadius = radius

        val s = cornerSmoothFactor

        // --- TOP edge ---
        val topEdgeLength = width - 2 * xRadius
        path.moveTo(left + xRadius, top)
        path.cubicTo(
            left + xRadius + topEdgeLength * 0.25f, top - bulgeAtEdge(0.25f),
            right - xRadius - topEdgeLength * 0.25f, top - bulgeAtEdge(0.75f),
            right - xRadius, top
        )

        // TOP-RIGHT corner avec smooth factor
        path.cubicTo(
            right - xRadius + xRadius * C * (1 - s), top,
            right, top + yRadius - yRadius * C * (1 - s),
            right, top + yRadius
        )

        // --- RIGHT edge ---
        val rightEdgeLength = height - 2 * yRadius
        path.cubicTo(
            right + bulgeAtEdge(0.25f), top + yRadius + rightEdgeLength * 0.25f,
            right + bulgeAtEdge(0.75f), top + yRadius + rightEdgeLength * 0.75f,
            right, bottom - yRadius
        )

        // BOTTOM-RIGHT corner
        path.cubicTo(
            right, bottom - yRadius + yRadius * C * (1 - s),
            right - xRadius + xRadius * C * (1 - s), bottom,
            right - xRadius, bottom
        )

        // --- BOTTOM edge ---
        val bottomEdgeLength = width - 2 * xRadius
        path.cubicTo(
            right - xRadius - bottomEdgeLength * 0.25f, bottom + bulgeAtEdge(0.75f),
            left + xRadius + bottomEdgeLength * 0.25f, bottom + bulgeAtEdge(0.25f),
            left + xRadius, bottom
        )

        // BOTTOM-LEFT corner
        path.cubicTo(
            left + xRadius - xRadius * C * (1 - s), bottom,
            left, bottom - yRadius + yRadius * C * (1 - s),
            left, bottom - yRadius
        )

        // --- LEFT edge ---
        val leftEdgeLength = height - 2 * yRadius
        path.cubicTo(
            left - bulgeAtEdge(0.75f), bottom - yRadius - leftEdgeLength * 0.25f,
            left - bulgeAtEdge(0.25f), top + yRadius + leftEdgeLength * 0.25f,
            left, top + yRadius
        )

        // TOP-LEFT corner
        path.cubicTo(
            left, top + yRadius - yRadius * C * (1 - s),
            left + xRadius - xRadius * C * (1 - s), top,
            left + xRadius, top
        )

        path.close()
        return Outline.Generic(path)
    }
}
