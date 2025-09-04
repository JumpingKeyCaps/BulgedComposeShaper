package com.lebaillyapp.bulgedcomposeshaper.composable

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
import androidx.compose.ui.unit.dp
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleSmoothShape
import kotlin.math.min




/**
 * A [Composable] that displays an [ImageBitmap] clipped into a custom
 * [Shape] with bulging edges.
 *
 * Internally, the image is automatically scaled up to ensure the bulge
 * deformation does not reveal empty background at the edges.
 * The zoom factor is computed based on the minimum image dimension and
 * the shape's `bulgeAmount`.
 *
 * #### Parameters
 * @param bitmap The [ImageBitmap] to display.
 * @param contentDescription Description of the image for accessibility.
 * @param shape The clipping [Shape].
 * Defaults to a [BulgedRectangleSmoothShape] with:
 * - corner radius = `10.dp`
 * - bulge amount = `0.07f`
 *
 * #### Notes
 * - Uses [ContentScale.Crop] to fill the available space.
 * - Applies a dynamic [graphicsLayer] scaling to cover bulge displacement.
 *
 *
 * @see BulgedRectangleSmoothShape
 * @see androidx.compose.foundation.Image
 */
@Composable
fun BulgedImage3(
    bitmap: ImageBitmap,
    contentDescription: String? = null,
    shape: Shape = BulgedRectangleSmoothShape(cornerRadius = 10.dp, bulgeAmount = 0.07f)
) {
    // Calcul dynamique du zoom pour couvrir le bulge
    val minDim = min(bitmap.width, bitmap.height).toFloat()
    val maxBulgePx = minDim * (shape as BulgedRectangleSmoothShape).bulgeAmount * 0.55f
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