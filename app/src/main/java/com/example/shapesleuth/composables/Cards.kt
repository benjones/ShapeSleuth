package com.example.shapesleuth.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shapesleuth.data.Card
import com.example.shapesleuth.data.Colors
import com.example.shapesleuth.data.Patterns
import com.example.shapesleuth.data.Shapes
import com.example.shapesleuth.data.makeDeck
import kotlin.random.Random

@Composable
fun CardView(card: Card, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Row(modifier = Modifier.fillMaxSize()
            .padding(4.dp)
            .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center) {
            Canvas(
                modifier = Modifier
                    .aspectRatio(1.0f)
                    .fillMaxSize()
                    .align(Alignment.CenterVertically),

                contentDescription = "Card:${card}"
            ) {
                when (card.shape) {
                    Shapes.Square -> {
                        drawRect(color = card.color.color)
                    }

                    else -> drawCircle(color = card.color.color)
                }
            }
        }
    }
}


@Preview
@Composable
fun CardPreview() {
    CardView(Card(Shapes.Triangle, Colors.Blue, Patterns.PolkaDot))
}

@Preview
@Composable
fun DeckPreview() {
    val deck = makeDeck(Random(7)) // Elway
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(4)
    ) {
        items(deck) { card ->
            CardView(card, modifier = Modifier.height(128.dp))
        }
    }
}