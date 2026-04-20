package com.example.shapesleuth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.shapesleuth.composables.Game
import com.example.shapesleuth.composables.GameScreen
import com.example.shapesleuth.data.GameState
import com.example.shapesleuth.ui.theme.ShapeSleuthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShapeSleuthTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val gameState = remember { GameState() }
                    GameScreen(gameState, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

