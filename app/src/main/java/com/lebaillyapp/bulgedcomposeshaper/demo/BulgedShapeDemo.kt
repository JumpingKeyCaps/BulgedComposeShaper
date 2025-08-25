package com.lebaillyapp.bulgedcomposeshaper.demo

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.bulgedcomposeshaper.R
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRectangleSmoothShape
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.old.BulgedRoundedRectangleShape
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
    var bulgeAmount by remember { mutableStateOf(0.00f) }
    var cornerSmoothFactor by remember { mutableStateOf(0.3f) }
    var autoMode by remember { mutableStateOf(true) }

    fun updateCornerAndSmooth(bulge: Float) {
        val clampedBulge = bulge.coerceIn(0f, 0.10f)
        cornerRadiusDp = 38f - (clampedBulge / 0.10f) * (38f - 5f)
        cornerSmoothFactor = 0.05f + (clampedBulge / 0.10f) * (0.40f - 0.10f)
    }

    if (autoMode) updateCornerAndSmooth(bulgeAmount)

    val shape = remember(cornerRadiusDp, bulgeAmount, cornerSmoothFactor) {
        BulgedRectangleSmoothShape(
            cornerRadius = cornerRadiusDp.dp,
            bulgeAmount = bulgeAmount,
            cornerSmoothFactor = cornerSmoothFactor
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .width(310.dp)
                .height(310.dp),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                BulgedImage3(
                    bitmap = imageBitmap,
                    contentDescription = "Image clippée avec forme bombée",
                    shape = shape
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Auto Mode:", color = Color.Black, fontSize = 12.sp)
                Switch(
                    modifier = Modifier.padding(start = 4.dp),
                    checked = autoMode,
                    onCheckedChange = { autoMode = it }
                )
            }

            Text("Corner Radius: ${cornerRadiusDp.roundToInt()}dp", color = Color.Black, fontSize = 12.sp)
            Slider(
                value = cornerRadiusDp,
                onValueChange = { cornerRadiusDp = it },
                valueRange = 0f..250f,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Bulge Amount: ${"%.2f".format(bulgeAmount)}", color = Color.Black, fontSize = 12.sp)
            Slider(
                value = bulgeAmount,
                onValueChange = {
                    bulgeAmount = it
                    if (autoMode) updateCornerAndSmooth(it)
                },
                valueRange = -0.1f..0.5f,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Corner Smooth Factor: ${"%.2f".format(cornerSmoothFactor)}", color = Color.Black, fontSize = 12.sp)
            Slider(
                value = cornerSmoothFactor,
                onValueChange = { cornerSmoothFactor = it },
                valueRange = 0f..0.5f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}