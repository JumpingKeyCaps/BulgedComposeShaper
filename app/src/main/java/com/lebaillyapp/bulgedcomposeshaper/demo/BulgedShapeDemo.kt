package com.lebaillyapp.bulgedcomposeshaper.demo

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.lebaillyapp.bulgedcomposeshaper.R
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleSmoothShape
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRoundedRectangleShape
import com.lebaillyapp.bulgedcomposeshaper.composable.BulgedImage
import com.lebaillyapp.bulgedcomposeshaper.composable.BulgedImage2
import com.lebaillyapp.bulgedcomposeshaper.composable.BulgedImage3
import kotlin.math.roundToInt

/**
 * Simple demo composable showcasing the usage of [BulgedRoundedRectangleShape].
 *
 * Displays a Card with the custom bulged shape applied, containing:
 * - a background image clipped to the shape
 */
@Composable
fun BulgedShapeDemo(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val imageBitmap: ImageBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.demopic).asImageBitmap()
    }

    var cornerRadiusDp by remember { mutableStateOf(30f) }
    var bulgeAmount by remember { mutableStateOf(0.03f) }
    var cornerSmoothFactor by remember { mutableStateOf(0.3f) }

    val shape = remember(cornerRadiusDp, bulgeAmount) {
        BulgedRectangleSmoothShape(cornerRadius = cornerRadiusDp.dp,
            bulgeAmount = bulgeAmount,
            cornerSmoothFactor = cornerSmoothFactor)
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .width(310.dp)
                .height(310.dp),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Élévation pour la Card du visualiseur
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                /**
                BulgedImage(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = imageBitmap,
                    contentDescription = "Image clippée avec forme bombée",
                    shape = shape
                )


                BulgedImage2(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = imageBitmap,
                    contentDescription = "")

                 */

                BulgedImage3(
                    bitmap = imageBitmap,
                    contentDescription = "Image clippée avec forme bombée",
                    shape = shape
                )

            }
        }





        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Corner Radius: ${cornerRadiusDp.roundToInt()}dp", color = Color.Black, fontWeight = FontWeight.Normal)
            Slider(
                value = cornerRadiusDp,
                onValueChange = { cornerRadiusDp = it },
                valueRange = 0f..250f
            )

            Text("Bulge Amount: ${"%.2f".format(bulgeAmount)}", color = Color.Black, fontWeight = FontWeight.Normal)
            Slider(
                value = bulgeAmount,
                onValueChange = { bulgeAmount = it },
                valueRange = -0.5f..0.5f
            )

            Text("Corner Smooth Factor: ${"%.2f".format(cornerSmoothFactor)}", color = Color.Black)
            Slider(
                value = cornerSmoothFactor,
                onValueChange = { cornerSmoothFactor = it },
                valueRange = 0f..0.5f
            )
        }
    }
}