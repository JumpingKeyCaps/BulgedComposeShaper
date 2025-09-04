package com.lebaillyapp.bulgedcomposeshaper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
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
import com.lebaillyapp.bulgedcomposeshaper.demo.BulgedAnimFullDemo
import com.lebaillyapp.bulgedcomposeshaper.demo.BulgedAnimShapeDemo
import com.lebaillyapp.bulgedcomposeshaper.demo.BulgedShapeDemo
import com.lebaillyapp.bulgedcomposeshaper.demo.SmoothBlobImageDemo
import com.lebaillyapp.bulgedcomposeshaper.ui.theme.BulgedComposeShaperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BulgedComposeShaperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(
                       0xFFECECEC
                   )
                   )) {
                       // 1 - Simple use demo
                     //  BulgedShapeDemo(modifier = Modifier.align(Alignment.Center))
                       // 2 - Animated demo
                       BulgedAnimShapeDemo(modifier = Modifier.align(Alignment.Center))
                       // 3 - Animated demo with idle mode
                 //      BulgedAnimFullDemo(modifier = Modifier.align(Alignment.Center))

                       // 4 - Animated demo blob
                /**
                       val context = LocalContext.current
                       val imageBitmap: ImageBitmap = remember {
                           BitmapFactory.decodeResource(context.resources, R.drawable.demopic).asImageBitmap()
                       }
                       var pointCount by remember { mutableStateOf(8f) }
                       var amplitude by remember { mutableStateOf(0.1f) }
                       var marginFactor by remember { mutableStateOf(0.85f) }
                       var randomFactor by remember { mutableStateOf(0.5f) }
                       Column(
                           modifier = Modifier
                               .fillMaxSize()
                               .padding(innerPadding)
                               .background(Color(0xFFECECEC)),
                           horizontalAlignment = Alignment.CenterHorizontally,
                           verticalArrangement = Arrangement.Center
                       ) {
                           SmoothBlobImageDemo(
                               bitmap = imageBitmap,
                               modifier = Modifier.size(250.dp),
                               pointCount = pointCount.toInt(),
                               amplitude = amplitude,
                               marginFactor = marginFactor,
                               randomFactor = randomFactor
                           )
                           Spacer(modifier = Modifier.height(24.dp))
                           Text(text = "Points: ${pointCount.toInt()}", color = Color.Black)
                           Slider(
                               value = pointCount,
                               onValueChange = { pointCount = it },
                               valueRange = 3f..50f,
                               steps = 47,
                               modifier = Modifier.padding(horizontal = 16.dp)
                           )
                           Text(text = "Amplitude: ${"%.2f".format(amplitude)}", color = Color.Black)
                           Slider(
                               value = amplitude,
                               onValueChange = { amplitude = it },
                               valueRange = 0f..0.5f,
                               modifier = Modifier.padding(horizontal = 16.dp)
                           )
                           Text(text = "Margin: ${"%.2f".format(marginFactor)}", color = Color.Black)
                           Slider(
                               value = marginFactor,
                               onValueChange = { marginFactor = it },
                               valueRange = 0.5f..1f,
                               modifier = Modifier.padding(horizontal = 16.dp)
                           )
                           Text(text = "Random Factor: ${"%.2f".format(randomFactor)}", color = Color.Black)
                           Slider(
                               value = randomFactor,
                               onValueChange = { randomFactor = it },
                               valueRange = 0f..1f,
                               modifier = Modifier.padding(horizontal = 16.dp)
                           )
                       }

                    */



                   }
                }
            }
        }
    }
}
