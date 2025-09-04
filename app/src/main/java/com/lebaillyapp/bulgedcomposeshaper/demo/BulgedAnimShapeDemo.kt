package com.lebaillyapp.bulgedcomposeshaper.demo

import android.graphics.BitmapFactory
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lebaillyapp.bulgedcomposeshaper.R
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleSmoothShape
import com.lebaillyapp.bulgedcomposeshaper.composable.BulgedImage3

@Composable
fun BulgedAnimShapeDemo(
    modifier: Modifier = Modifier,
    idleMode: Boolean = true
) {
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.demopic).asImageBitmap()
    }

    var pressed by remember { mutableStateOf(false) }

    // Valeurs animées
    val (bulgeTarget, radiusTarget, smoothTarget) = if (idleMode) {
        // Animation idle en boucle
        val infiniteTransition = rememberInfiniteTransition()
        val bulge by infiniteTransition.animateFloat(
            initialValue = -0.10f,
            targetValue = -0.10f,
            animationSpec = infiniteRepeatable(
                animation = tween(3500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val radius by infiniteTransition.animateFloat(
            initialValue = 220f,
            targetValue = 220f,
            animationSpec = infiniteRepeatable(
                animation = tween(3500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val radiusDp = radius.dp

        val smooth by infiniteTransition.animateFloat(
            initialValue = -0.40f,
            targetValue = 0.50f,
            animationSpec = infiniteRepeatable(
                animation = tween(3500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Triple(bulge, radiusDp, smooth)
    } else {
        // Press/release animation
        val bulgeAnim by animateFloatAsState(
            targetValue = if (pressed) -0.00f else 0.15f,
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
        val radiusAnim by animateDpAsState(
            targetValue = if (pressed) 220.dp else 220.dp,
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
        val smoothAnim by animateFloatAsState(
            targetValue = if (pressed) -0.3f else 0.5f,
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
        Triple(bulgeAnim, radiusAnim, smoothAnim)
    }

    val shape = remember(bulgeTarget, radiusTarget, smoothTarget) {
        BulgedRectangleSmoothShape(
            cornerRadius = radiusTarget,
            bulgeAmount = bulgeTarget,
            cornerSmoothFactor = smoothTarget
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .width(220.dp)
                .height(220.dp)
                .shadow(6.dp, shape)
                .pointerInput(idleMode) {
                    if (!idleMode) {
                        detectTapGestures(
                            onPress = {
                                pressed = true
                                try { awaitRelease() } finally { pressed = false }
                            }
                        )
                    }
                },
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                BulgedImage3(
                    bitmap = imageBitmap,
                    contentDescription = "Image clippée animée",
                    shape = shape
                )
            }
        }
    }
}