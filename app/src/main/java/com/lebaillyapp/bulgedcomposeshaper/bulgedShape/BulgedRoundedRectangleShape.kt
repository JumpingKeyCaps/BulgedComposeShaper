package com.lebaillyapp.bulgedcomposeshaper.bulgedShape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp

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
    private val bulgeAmount: Float = 0.05f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            val width = size.width
            val height = size.height

            // Convert corner radius from Dp to pixels
            val cornerRadiusPx = with(density) { cornerRadius.toPx() }

            // Clamp corner radius to at most half of the smallest dimension
            val actualCornerRadius = cornerRadiusPx.coerceAtMost(minOf(width, height) / 2f)

            // Calculate bulge amplitude as a fraction of the smallest dimension
            val actualBulgeAmount = minOf(width, height) * bulgeAmount

            val left = 0f
            val top = 0f
            val right = width
            val bottom = height

            // Start at top edge, right before the top-right corner arc
            moveTo(right - actualCornerRadius, top)

            // 1. Top-right corner arc
            arcTo(
                rect = Rect(right - 2 * actualCornerRadius, top, right, top + 2 * actualCornerRadius),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // 2. Right side bulged cubic bezier
            cubicTo(
                x1 = right + actualBulgeAmount,
                y1 = top + actualCornerRadius + (bottom - 2 * actualCornerRadius - (top + actualCornerRadius)) * 0.33f,
                x2 = right + actualBulgeAmount,
                y2 = top + actualCornerRadius + (bottom - 2 * actualCornerRadius - (top + actualCornerRadius)) * 0.66f,
                x3 = right,
                y3 = bottom - actualCornerRadius
            )

            // 3. Bottom-right corner arc
            arcTo(
                rect = Rect(right - 2 * actualCornerRadius, bottom - 2 * actualCornerRadius, right, bottom),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // 4. Bottom side bulged cubic bezier
            cubicTo(
                x1 = right - actualCornerRadius - (right - 2 * actualCornerRadius - (left + actualCornerRadius)) * 0.33f,
                y1 = bottom + actualBulgeAmount,
                x2 = right - actualCornerRadius - (right - 2 * actualCornerRadius - (left + actualCornerRadius)) * 0.66f,
                y2 = bottom + actualBulgeAmount,
                x3 = left + actualCornerRadius,
                y3 = bottom
            )

            // 5. Bottom-left corner arc
            arcTo(
                rect = Rect(left, bottom - 2 * actualCornerRadius, left + 2 * actualCornerRadius, bottom),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // 6. Left side bulged cubic bezier
            cubicTo(
                x1 = left - actualBulgeAmount,
                y1 = bottom - actualCornerRadius - (bottom - 2 * actualCornerRadius - (top + actualCornerRadius)) * 0.33f,
                x2 = left - actualBulgeAmount,
                y2 = bottom - actualCornerRadius - (bottom - 2 * actualCornerRadius - (top + actualCornerRadius)) * 0.66f,
                x3 = left,
                y3 = top + actualCornerRadius
            )

            // 7. Top-left corner arc
            arcTo(
                rect = Rect(left, top, left + 2 * actualCornerRadius, top + 2 * actualCornerRadius),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // 8. Top side bulged cubic bezier
            cubicTo(
                x1 = left + actualCornerRadius + (right - 2 * actualCornerRadius - (left + actualCornerRadius)) * 0.33f,
                y1 = top - actualBulgeAmount,
                x2 = left + actualCornerRadius + (right - 2 * actualCornerRadius - (left + actualCornerRadius)) * 0.66f,
                y2 = top - actualBulgeAmount,
                x3 = right - actualCornerRadius,
                y3 = top
            )

            close()
        })
    }
}