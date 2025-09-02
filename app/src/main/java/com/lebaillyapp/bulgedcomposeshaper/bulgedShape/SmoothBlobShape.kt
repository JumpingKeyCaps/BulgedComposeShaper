package com.lebaillyapp.bulgedcomposeshaper.bulgedShape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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