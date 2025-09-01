package com.lebaillyapp.bulgedcomposeshaper.demo

import android.graphics.BitmapFactory
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
fun BulgedAnimShapeDemo(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.demopic).asImageBitmap()
    }

    // Etat pressé ou pas
    var pressed by remember { mutableStateOf(false) }

    // Animations synchronisées
    val bulgeAnim by animateFloatAsState(
        targetValue = if (pressed) 0.12f else -0.05f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "bulge"
    )
    val radiusAnim by animateDpAsState(
        targetValue = if (pressed) 220.dp else 220.dp,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "radius"
    )
    val smoothAnim by animateFloatAsState(
        targetValue = if (pressed) 0.3f else -0.3f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "smooth"
    )

    // Shape dynamique
    val shape = remember(bulgeAnim, radiusAnim, smoothAnim) {
        BulgedRectangleSmoothShape(
            cornerRadius = radiusAnim,
            bulgeAmount = bulgeAnim,
            cornerSmoothFactor = smoothAnim
        )
    }

    // Conteneur centré
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
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            try {
                                // Attend que le doigt soit relâché
                                awaitRelease()
                            } finally {
                                pressed = false
                            }
                        }
                    )
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