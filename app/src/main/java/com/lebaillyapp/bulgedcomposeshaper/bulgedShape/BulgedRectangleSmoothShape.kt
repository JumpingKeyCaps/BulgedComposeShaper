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
