package com.lebaillyapp.bulgedcomposeshaper.composable

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.lebaillyapp.bulgedcomposeshaper.R // Assurez-vous que R est importé correctement

/**
 * A Composable that displays an ImageBitmap clipped to a custom Shape,
 * leveraging a passthrough AGSL shader to ensure correct clipping of complex shapes.
 *
 * This component addresses a known Compose rendering behavior where complex custom shapes
 * (Outline.Generic) might not be fully clipped by default Modifier.clip() or graphicsLayer
 * without a RenderEffect. By applying a simple passthrough shader, it forces Compose
 * to use the rendering pipeline that correctly handles complex shape clipping.
 *
 * @param modifier The modifier to be applied to this Composable.
 * @param bitmap The ImageBitmap to be displayed and clipped.
 * @param contentDescription A description of the image for accessibility.
 * @param contentScale The scale to be used for the image content.
 * @param shape The custom Shape to clip the image to. Defaults to RectangleShape.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BulgedImage(
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RectangleShape
) {
    val context = LocalContext.current

    // Load the passthrough shader code once.
    // This shader simply returns the input image without modification,
    // but its presence in graphicsLayer ensures correct complex shape clipping.
    val passthroughShaderCode = remember {
        context.resources.openRawResource(R.raw.default_shader).bufferedReader().use { it.readText() }
    }
    val passthroughShader = remember { RuntimeShader(passthroughShaderCode) }

    // Create the RenderEffect for the passthrough shader.
    val passthroughRenderEffect = remember(passthroughShader) {
        RenderEffect.createRuntimeShaderEffect(passthroughShader, "inputShader").asComposeRenderEffect()
    }

    Image(
        painter = androidx.compose.ui.graphics.painter.BitmapPainter(bitmap),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .fillMaxSize()
            // Apply the passthrough shader via graphicsLayer, passing the shape for clipping.
            // This forces the rendering pipeline to correctly clip the complex shape.
            .graphicsLayer(renderEffect = passthroughRenderEffect, clip = true, shape = shape)
    )
}