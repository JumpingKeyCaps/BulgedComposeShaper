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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lebaillyapp.bulgedcomposeshaper.demo.BulgedAnimFullDemo
import com.lebaillyapp.bulgedcomposeshaper.demo.BulgedAnimShapeDemo
import com.lebaillyapp.bulgedcomposeshaper.demo.BulgedShapeDemo
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
                   //    BulgedAnimShapeDemo(modifier = Modifier.align(Alignment.Center))
                       // 3 - Animated demo with idle mode
                       BulgedAnimFullDemo(modifier = Modifier.align(Alignment.Center))
                   }
                }
            }
        }
    }
}
