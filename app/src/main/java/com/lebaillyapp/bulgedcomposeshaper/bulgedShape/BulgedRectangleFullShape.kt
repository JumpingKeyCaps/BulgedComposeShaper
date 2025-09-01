package com.lebaillyapp.bulgedcomposeshaper.bulgedShape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.min

// Config en Dp pour l'utilisateur
data class CornerConfig(
    val topLeft: Dp,
    val topRight: Dp,
    val bottomRight: Dp,
    val bottomLeft: Dp
)

// Config des bords (bulge) en float
data class EdgeBulge(
    val top: Float,
    val right: Float,
    val bottom: Float,
    val left: Float
)

// Conversion interne Dp -> Px
private data class CornerPx(
    val topLeft: Float,
    val topRight: Float,
    val bottomRight: Float,
    val bottomLeft: Float
)

class BulgedRectangleFullShape(
    private val cornerRadius: CornerConfig,
    val bulgeAmount: EdgeBulge,
    private val cornerSmoothFactor: Float = 0.3f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val width = size.width
        val height = size.height

        val radiiPx = CornerPx(
            topLeft = with(density) { cornerRadius.topLeft.toPx() },
            topRight = with(density) { cornerRadius.topRight.toPx() },
            bottomRight = with(density) { cornerRadius.bottomRight.toPx() },
            bottomLeft = with(density) { cornerRadius.bottomLeft.toPx() }
        )

        val left = 0f
        val top = 0f
        val right = width
        val bottom = height
        val s = cornerSmoothFactor
        val C = 0.5522847498f

        // --- Helper ---
        fun bulgeAtEdge(t: Float, edge: Float) = min(width, height) * edge * 0.5f * sin(t * PI).toFloat()

        // --- TOP edge ---
        val topEdgeLength = width - radiiPx.topLeft - radiiPx.topRight
        path.moveTo(left + radiiPx.topLeft, top)
        path.cubicTo(
            left + radiiPx.topLeft + topEdgeLength * 0.25f, top - bulgeAtEdge(0.25f, bulgeAmount.top),
            right - radiiPx.topRight - topEdgeLength * 0.25f, top - bulgeAtEdge(0.75f, bulgeAmount.top),
            right - radiiPx.topRight, top
        )

        // TOP-RIGHT corner
        path.cubicTo(
            right - radiiPx.topRight + radiiPx.topRight * C * (1 - s), top,
            right, top + radiiPx.topRight - radiiPx.topRight * C * (1 - s),
            right, top + radiiPx.topRight
        )

        // --- RIGHT edge ---
        val rightEdgeLength = height - radiiPx.topRight - radiiPx.bottomRight
        path.cubicTo(
            right + bulgeAtEdge(0.25f, bulgeAmount.right), top + radiiPx.topRight + rightEdgeLength * 0.25f,
            right + bulgeAtEdge(0.75f, bulgeAmount.right), bottom - radiiPx.bottomRight - rightEdgeLength * 0.25f,
            right, bottom - radiiPx.bottomRight
        )

        // BOTTOM-RIGHT corner
        path.cubicTo(
            right, bottom - radiiPx.bottomRight + radiiPx.bottomRight * C * (1 - s),
            right - radiiPx.bottomRight + radiiPx.bottomRight * C * (1 - s), bottom,
            right - radiiPx.bottomRight, bottom
        )

        // --- BOTTOM edge ---
        val bottomEdgeLength = width - radiiPx.bottomLeft - radiiPx.bottomRight
        path.cubicTo(
            right - radiiPx.bottomRight - bottomEdgeLength * 0.25f, bottom + bulgeAtEdge(0.75f, bulgeAmount.bottom),
            left + radiiPx.bottomLeft + bottomEdgeLength * 0.25f, bottom + bulgeAtEdge(0.25f, bulgeAmount.bottom),
            left + radiiPx.bottomLeft, bottom
        )

        // BOTTOM-LEFT corner
        path.cubicTo(
            left + radiiPx.bottomLeft - radiiPx.bottomLeft * C * (1 - s), bottom,
            left, bottom - radiiPx.bottomLeft + radiiPx.bottomLeft * C * (1 - s),
            left, bottom - radiiPx.bottomLeft
        )

        // --- LEFT edge ---
        val leftEdgeLength = height - radiiPx.topLeft - radiiPx.bottomLeft
        path.cubicTo(
            left - bulgeAtEdge(0.75f, bulgeAmount.left), bottom - radiiPx.bottomLeft - leftEdgeLength * 0.25f,
            left - bulgeAtEdge(0.25f, bulgeAmount.left), top + radiiPx.topLeft + leftEdgeLength * 0.25f,
            left, top + radiiPx.topLeft
        )

        // TOP-LEFT corner
        path.cubicTo(
            left, top + radiiPx.topLeft - radiiPx.topLeft * C * (1 - s),
            left + radiiPx.topLeft - radiiPx.topLeft * C * (1 - s), top,
            left + radiiPx.topLeft, top
        )

        path.close()
        return Outline.Generic(path)
    }
}