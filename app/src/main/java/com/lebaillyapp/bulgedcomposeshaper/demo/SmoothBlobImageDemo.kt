package com.lebaillyapp.bulgedcomposeshaper.demo

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.SmoothBlobShape
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SmoothBlobImageDemo(
    bitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    pointCount: Int = 8,
    amplitude: Float = 0.1f,
    marginFactor: Float = 0.85f,
    randomFactor: Float = 0.5f // contrôle la variation aléatoire
) {
    // offsets aléatoires pour chaque point (fixe, mais modulable)
    val randomOffsets = remember(pointCount) {
        List(pointCount) { kotlin.random.Random.nextFloat() }
    }

    // rayons relatifs pour chaque point
    val radiiFractions = List(pointCount) { i ->
        1f - amplitude / 2 + amplitude * randomOffsets[i] * randomFactor
    }

    val blobShape = remember(radiiFractions, marginFactor) {
        SmoothBlobShape(radiiFractions, marginFactor)
    }

    Box(
        modifier = modifier
            .clip(blobShape)
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}