package com.lebaillyapp.bulgedcomposeshaper.demo

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.lebaillyapp.bulgedcomposeshaper.R
import com.lebaillyapp.bulgedcomposeshaper.bulgedShape.BulgedRoundedRectangleShape
import com.lebaillyapp.bulgedcomposeshaper.composable.BulgedImage

/**
 * Simple demo composable showcasing the usage of [BulgedRoundedRectangleShape].
 *
 * Displays a Card with the custom bulged shape applied, containing:
 * - a background image clipped to the shape
 */
@Composable
fun BulgedShapeDemo(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Chargez l'ImageBitmap
    val imageBitmap: ImageBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.demopic).asImageBitmap()
    }

    // Créez une instance de votre forme bombée personnalisée
    val bulgedShape = remember {
        BulgedRoundedRectangleShape(
            cornerRadius = 40.dp,
            bulgeAmount = 0.03f // Ajustez cette valeur pour un bombement plus ou moins prononcé
        )
    }

    Card(
        modifier = modifier.size(320.dp),
        shape = bulgedShape, // La Card utilise la forme pour son propre fond et élévation
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        BulgedImage(
            modifier = Modifier.fillMaxSize(),
            bitmap = imageBitmap,
            contentDescription = "Image clippée avec forme bombée",
            contentScale = ContentScale.Crop,
            shape = bulgedShape // Passez la forme à BulgedImage
        )
    }
}