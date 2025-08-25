package com.lebaillyapp.bulgedcomposeshaper.bulgedShape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

/**
 * A custom [Shape] that renders a rounded rectangle with gently bulging sides.
 *
 * Each side of the rectangle can curve outward (convex) based on a configurable bulge factor.
 * This creates a more organic and fluid look compared to traditional sharp or flat-edged rectangles.
 *
 * Designed to be used in Jetpack Compose UIs where subtle visual personality is desired —
 * like interactive surfaces, image clipping, or animated shader effects.
 *
 * @param cornerRadius The corner radius in [Dp]. Ensures rounded corners that blend smoothly with the bulged sides.
 * @param bulgeAmount The relative strength of the bulge on each side.
 * - `0f` means flat edges (classic rounded rectangle)
 * - Positive values (e.g., `0.05f`) create outward bulges.
 *
 * The bulge is computed as a proportion of the smallest dimension (width or height),
 * so it scales responsively with different layout sizes.
 *
 * Example usage:
 * ```
 * shape = BulgedRoundedRectangleShape(cornerRadius = 24.dp, bulgeAmount = 0.07f)
 * ```
 *
 * Note: Bulging is applied uniformly to all four sides.
 */

class BulgedRoundedRectangleShape(
    private val cornerRadius: Dp,
    val bulgeAmount: Float = 0f // 0 = flat, positive = outward, negative = cave
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

        fun bulgeAtEdge(t: Float): Float = maxBulgePx * sin(t * PI).toFloat() // 0 au début et fin, max au milieu

        val left = 0f
        val top = 0f
        val right = width
        val bottom = height
        val xRadius = radius
        val yRadius = radius

        // --- TOP edge ---
        val topEdgeLength = width - 2 * xRadius
        path.moveTo(left + xRadius, top)
        path.cubicTo(
            left + xRadius + topEdgeLength * 0.25f, top - bulgeAtEdge(0.25f),
            left + xRadius + topEdgeLength * 0.75f, top - bulgeAtEdge(0.75f),
            right - xRadius, top
        )

        // TOP-RIGHT corner
        path.cubicTo(
            right - xRadius + xRadius * C, top,
            right, top + yRadius - yRadius * C,
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
            right, bottom - yRadius + yRadius * C,
            right - xRadius + xRadius * C, bottom,
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
            left + xRadius - xRadius * C, bottom,
            left, bottom - yRadius + yRadius * C,
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
            left, top + yRadius - yRadius * C,
            left + xRadius - xRadius * C, top,
            left + xRadius, top
        )

        path.close()
        return Outline.Generic(path)
    }
}




