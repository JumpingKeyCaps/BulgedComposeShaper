package com.lebaillyapp.bulgedcomposeshaper.demo

import android.graphics.BitmapFactory
import androidx.compose.animation.core.*
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
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleFullShape
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.CornerConfig
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.EdgeBulge
import com.lebaillyapp.bulgedcomposeshaper.composable.BulgedImageFull

@Composable
fun BulgedAnimFullDemo(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.demopic).asImageBitmap()
    }

    // --- Demo parameters ---
    val demoCorners = CornerConfig(
        topLeft = 40.dp,
        topRight = 40.dp,
        bottomRight = 40.dp,
        bottomLeft = 40.dp
    )
    val demoBulges = EdgeBulge(
        top = 0.10f,
        right = 0.0f,
        bottom = 0.0f,
        left = 0.0f
    )
    val cornerSmooth = 0.1f

    // --- Shape statique ---
    val demoShape = BulgedRectangleFullShape(
        cornerRadius = demoCorners,
        bulgeAmount = demoBulges,
        cornerSmoothFactor = cornerSmooth
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .width(220.dp)
                .height(220.dp)
                .shadow(6.dp, demoShape),
            shape = demoShape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            BulgedImageFull(
                bitmap = imageBitmap,
                contentDescription = "Demo image full shape",
                shape = demoShape
            )
        }
    }
}
