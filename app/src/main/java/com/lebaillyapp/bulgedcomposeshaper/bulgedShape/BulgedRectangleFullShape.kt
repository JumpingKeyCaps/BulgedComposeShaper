package com.lebaillyapp.bulgedcomposeshaper.bulgedShape



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

data class CornerConfig(
    val topLeft: Dp,
    val topRight: Dp,
    val bottomRight: Dp,
    val bottomLeft: Dp
)

data class CornerSmoothConfig(
    val topLeft: Float,
    val topRight: Float,
    val bottomRight: Float,
    val bottomLeft: Float
)

data class EdgeBulge(
    val top: Float,
    val right: Float,
    val bottom: Float,
    val left: Float
)

private data class CornerPx(
    val topLeft: Float,
    val topRight: Float,
    val bottomRight: Float,
    val bottomLeft: Float
)




/**
 * A custom [Shape] that draws a rectangle with independently rounded corners
 * and optional bulging edges.
 *
 * The resulting outline is built with cubic Bézier curves, allowing:
 * - Per-corner radius (in [Dp])
 * - Per-corner smoothness control (`0f..1f`), to adjust how "round" or "sharp" the arc blends
 * - Per-edge bulge factor (`-∞..+∞`), which pushes edges outward (positive) or inward (negative)
 *
 * Bulges are applied using a sine-based distortion relative to the edge length,
 * scaled by the smallest dimension of the shape (width or height).
 *
 * ### Usage
 * Use this when you want rounded rectangles with organic distortions,
 * e.g. playful cards, animated blobs, or custom buttons.
 *
 * ### Parameters
 * @property cornerRadius Per-corner radii in [Dp]. Controls how much each corner is rounded.
 * @property bulgeAmount Per-edge bulge intensity.
 * Values are floats where:
 * - `0f` → no bulge (flat edge)
 * - `>0f` → outward bump
 * - `<0f` → inward dip
 * Scaling is relative to half of the smallest dimension of the shape.
 * @property cornerSmoothConfig Per-corner smoothing factor (`0f..1f`).
 * Controls curvature of the rounded corner:
 * - `0f` → sharp circular arc
 * - `1f` → very smooth and wide transition
 *
 * ### Notes
 * - All smoothness values are clamped into `[0f, 1f]`.
 * - The algorithm uses a constant `C ≈ 0.55228` for Bézier circle approximation.
 * - Bulges are applied symmetrically along each edge using a sine wave.
 *
 * @see Shape
 * @see Outline.Generic
 */
class BulgedRectangleFullShape(
    private val cornerRadius: CornerConfig,
    val bulgeAmount: EdgeBulge,
    private val cornerSmoothConfig: CornerSmoothConfig = CornerSmoothConfig(0.3f, 0.3f, 0.3f, 0.3f)
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

        val C = 0.5522847498f
        val left = 0f
        val top = 0f
        val right = width
        val bottom = height

        // Clamp smooth factors entre 0 et 1
        val sTL = cornerSmoothConfig.topLeft.coerceIn(0f, 1f)
        val sTR = cornerSmoothConfig.topRight.coerceIn(0f, 1f)
        val sBR = cornerSmoothConfig.bottomRight.coerceIn(0f, 1f)
        val sBL = cornerSmoothConfig.bottomLeft.coerceIn(0f, 1f)

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
            right - radiiPx.topRight + radiiPx.topRight * C * (1 - sTR), top,
            right, top + radiiPx.topRight - radiiPx.topRight * C * (1 - sTR),
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
            right, bottom - radiiPx.bottomRight + radiiPx.bottomRight * C * (1 - sBR),
            right - radiiPx.bottomRight + radiiPx.bottomRight * C * (1 - sBR), bottom,
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
            left + radiiPx.bottomLeft - radiiPx.bottomLeft * C * (1 - sBL), bottom,
            left, bottom - radiiPx.bottomLeft + radiiPx.bottomLeft * C * (1 - sBL),
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
            left, top + radiiPx.topLeft - radiiPx.topLeft * C * (1 - sTL),
            left + radiiPx.topLeft - radiiPx.topLeft * C * (1 - sTL), top,
            left + radiiPx.topLeft, top
        )

        path.close()
        return Outline.Generic(path)
    }
}