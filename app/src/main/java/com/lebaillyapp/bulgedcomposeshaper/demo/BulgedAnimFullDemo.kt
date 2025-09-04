package com.lebaillyapp.bulgedcomposeshaper.demo

import android.graphics.BitmapFactory
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lebaillyapp.bulgedcomposeshaper.R
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.*
import com.lebaillyapp.bulgedcomposeshaper.composable.BulgedImageFull

/**
 * Demo [Composable] showcasing the use of [BulgedImageFull] with a
 * [BulgedRectangleFullShape].
 *
 * ### Behavior
 * - Loads a demo image (`R.drawable.demopic`) into an [ImageBitmap].
 * - Applies a custom bulged shape with:
 *   - Corner radii ([CornerConfig])
 *   - Per-edge bulges ([EdgeBulge])
 *   - Corner smoothing ([CornerSmoothConfig])
 * - Displays the image inside a [Card] that uses the same shape for both
 *   clipping and shadow casting.
 *
 * ### Visual Effect
 * The result is a clipped image with organic “bulged” edges, framed by a card
 * container. The card has:
 * - Fixed dimensions (220×220 dp)
 * - Drop shadow applied using the same shape
 * - White background color for contrast
 *
 * ### Parameters
 * @param modifier The [Modifier] for external layout control.
 *
 * ### Notes
 * - Currently, the shape parameters are hardcoded for demonstration.
 * - The class name includes "Anim" since it's intended as a playground for
 *   adding [animate*AsState] or [InfiniteTransition] later to animate
 *   bulges, corners, or smoothness.
 * - Useful for previews, prototyping, or verifying rendering of custom
 *   bulged shapes with images.
 *
 * ### Example
 * ```kotlin
 * BulgedAnimFullDemo(
 *     modifier = Modifier.fillMaxSize()
 * )
 * ```
 *
 * @see BulgedImageFull
 * @see BulgedRectangleFullShape
 * @see EdgeBulge
 * @see CornerConfig
 * @see CornerSmoothConfig
 */
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
        topLeft = 110.dp,
        topRight = 110.dp,
        bottomRight = 110.dp,
        bottomLeft = 110.dp
    )
    val demoBulges = EdgeBulge(
        top = 0.10f,
        right = 0.05f,
        bottom = 0.15f,
        left = 0.08f
    )
    val demoSmooth = CornerSmoothConfig(
        topLeft = 0.4f,
        topRight = 0.4f,
        bottomRight = 0.4f,
        bottomLeft = 0.4f
    )

    val demoShape = BulgedRectangleFullShape(
        cornerRadius = demoCorners,
        bulgeAmount = demoBulges,
        cornerSmoothConfig = demoSmooth
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
