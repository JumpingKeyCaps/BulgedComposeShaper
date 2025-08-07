package com.lebaillyapp.bulgedcomposeshaper.composable

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.lebaillyapp.bulgedcomposeshaper.R // Assurez-vous que R est importé correctement
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRoundedRectangleShape

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
    contentScale: ContentScale = ContentScale.Crop, // <<--- LAISSE ContentScale.Crop ICI !
    shape: Shape = BulgedRoundedRectangleShape(cornerRadius = 10.dp, bulgeAmount = 0.02f)
) {
    val context = LocalContext.current

    val shaderCode = remember {
        context.resources.openRawResource(R.raw.default_shader).bufferedReader().use { it.readText() }
    }
    val shader = remember { RuntimeShader(shaderCode) }

    var composableSize by remember { mutableStateOf(Size.Zero) }

    SideEffect {
        if (composableSize.width > 0f && composableSize.height > 0f) {
            shader.setFloatUniform("uResolution", composableSize.width, composableSize.height)
            // Retire uImageSize car le shader n'en a plus besoin pour le cropping,
            // seulement pour le clamp.
            // Le shader n'a besoin de rien d'autre que uResolution pour le clamp.
        }
    }

    val renderEffect = remember(shader, composableSize) {
        RenderEffect.createRuntimeShaderEffect(shader, "inputShader").asComposeRenderEffect()
    }

    Image(
        painter = BitmapPainter(bitmap),
        contentDescription = contentDescription,
        contentScale = contentScale, // <<--- CE ContentScale.Crop EST LA VRAIE CLÉ !
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { newSize ->
                composableSize = newSize.toSize()
            }
            .graphicsLayer(renderEffect = renderEffect, clip = true, shape = shape)
    )
}