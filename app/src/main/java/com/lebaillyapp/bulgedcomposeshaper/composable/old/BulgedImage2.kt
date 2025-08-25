package com.lebaillyapp.bulgedcomposeshaper.composable.old

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.lebaillyapp.bulgedcomposeshaper.R
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.old.BulgedRoundedRectangleShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// ===== 1. FONCTION UTILITAIRE POUR GÉNÉRER LE MASQUE =====
fun generateBulgeMask(
    width: Int,
    height: Int,
    cornerRadius: Dp,
    bulgeAmount: Float,
    density: Density
): ImageBitmap {
    val shape = BulgedRoundedRectangleShape(cornerRadius, bulgeAmount)
    val outline = shape.createOutline(
        Size(width.toFloat(), height.toFloat()),
        LayoutDirection.Ltr,
        density
    )

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = android.graphics.Color.WHITE
        isAntiAlias = true
    }

    when (outline) {
        is Outline.Generic -> {
            canvas.drawPath(outline.path.asAndroidPath(), paint)
        }
        is Outline.Rectangle -> {
            canvas.drawRect(outline.rect.toAndroidRect(), paint)
        }
        is Outline.Rounded -> {
            canvas.drawRoundRect(
                outline.roundRect.toAndroidRectF(),
                outline.roundRect.topLeftCornerRadius.x,
                outline.roundRect.topLeftCornerRadius.y,
                paint
            )
        }
    }

    return bitmap.asImageBitmap()
}

// ===== 2. COMPOSABLE OPTIMISÉE =====
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BulgedImage2(
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
    cornerRadius: Dp = 10.dp,
    bulgeAmount: Float = 0.02f
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    // Shader code une seule fois
    val shaderCode = remember {
        context.resources.openRawResource(R.raw.enhanced_shader)
            .bufferedReader()
            .use { it.readText() }
    }
    val shader = remember { RuntimeShader(shaderCode) }

    var composableSize by remember { mutableStateOf(Size.Zero) }
    var maskBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Génère le masque quand la taille change
    LaunchedEffect(composableSize, cornerRadius, bulgeAmount) {
        if (composableSize.width > 0f && composableSize.height > 0f) {
            withContext(Dispatchers.Default) {
                val mask = generateBulgeMask(
                    width = composableSize.width.toInt(),
                    height = composableSize.height.toInt(),
                    cornerRadius = cornerRadius,
                    bulgeAmount = bulgeAmount,
                    density = density
                )
                maskBitmap = mask
            }
        }
    }

    // Met à jour les uniformes du shader
    SideEffect {
        if (composableSize.width > 0f && composableSize.height > 0f) {
            shader.setFloatUniform("uResolution", composableSize.width, composableSize.height)
        }
    }

    // Crée l'effet de rendu seulement si on a le masque
    val renderEffect = remember(shader, maskBitmap) {
        maskBitmap?.let { mask ->
            // Passe le masque comme texture au shader
            shader.setInputBuffer("maskTexture",
                BitmapShader(mask.asAndroidBitmap(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            )
            RenderEffect.createRuntimeShaderEffect(shader, "inputShader").asComposeRenderEffect()
        }
    }

    Image(
        painter = BitmapPainter(bitmap),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { newSize ->
                composableSize = newSize.toSize()
            }
            .graphicsLayer {
                renderEffect?.let { this.renderEffect = it }
                clip = false // Le shader fait le clipping
            }
    )
}

// ===== 3. EXTENSIONS UTILITAIRES =====
private fun androidx.compose.ui.geometry.Rect.toAndroidRect(): Rect {
    return Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
}

private fun RoundRect.toAndroidRectF(): RectF {
    return RectF(left, top, right, bottom)
}