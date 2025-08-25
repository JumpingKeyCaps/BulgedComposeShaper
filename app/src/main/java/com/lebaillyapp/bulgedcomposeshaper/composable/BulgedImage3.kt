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
import androidx.compose.ui.unit.dp
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleSmoothShape
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRoundedRectangleShape
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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