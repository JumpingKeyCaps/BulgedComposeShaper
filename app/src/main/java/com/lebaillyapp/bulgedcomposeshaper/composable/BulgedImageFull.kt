package com.lebaillyapp.bulgedcomposeshaper.composable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleFullShape
import kotlin.math.max
import kotlin.math.min
/**
 * A [Composable] that displays an [ImageBitmap] clipped into a custom
 * bulged [Shape], with automatic zoom to compensate for edge deformation.
 *
 * This version supports both:
 * - [BulgedRectangleFullShape] (per-edge bulges)
 * - [BulgedRectangleSmoothShape] (uniform bulge)
 *
 * The maximum bulge among all edges is used to compute a zoom factor
 * so the image fully covers the distorted outline without leaving gaps.
 *
 * #### Parameters
 * @param bitmap The [ImageBitmap] to display.
 * @param contentDescription Description of the image for accessibility.
 * @param shape The clipping [Shape].
 * Must be a [BulgedRectangleFullShape], [BulgedRectangleSmoothShape],
 * or any other [Shape] (no bulge compensation will be applied in the latter case).
 *
 * #### Notes
 * - Uses [ContentScale.Crop] to ensure coverage of the composable area.
 * - The zoom factor is proportional to the largest bulge amount detected.
 * - Safe to use with any [Shape], but only bulged shapes trigger scaling.
 *
 *
 * @see BulgedRectangleFullShape
 * @see com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleSmoothShape
 * @see androidx.compose.foundation.Image
 */
@Composable
fun BulgedImageFull(
    bitmap: ImageBitmap,
    contentDescription: String? = null,
    shape: Shape
) {
    val minDim = min(bitmap.width, bitmap.height).toFloat()

    // Calcul du max bulge pour le zoom, compatible avec FullShape
    val maxBulge = when (shape) {
        is BulgedRectangleFullShape -> {
            listOf(
                shape.bulgeAmount.top,
                shape.bulgeAmount.right,
                shape.bulgeAmount.bottom,
                shape.bulgeAmount.left
            ).maxOrNull() ?: 0f
        }
        is com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleSmoothShape -> {
            shape.bulgeAmount
        }
        else -> 0f
    }

    // Zoom proportionnel au bulge
    val maxBulgePx = minDim * maxBulge * 0.55f
    val zoomFactor = 1f + maxBulgePx / minDim

    Image(
        painter = BitmapPainter(bitmap),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = zoomFactor
                scaleY = zoomFactor
            }
            .clip(shape)
    )
}
