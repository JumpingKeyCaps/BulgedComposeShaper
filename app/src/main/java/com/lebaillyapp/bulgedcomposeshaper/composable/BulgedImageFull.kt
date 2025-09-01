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
