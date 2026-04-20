package com.example.shapesleuth.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.shapesleuth.data.Card
import com.example.shapesleuth.data.makeDeck
import kotlin.random.Random


import com.example.shapesleuth.data.GameState

@Composable
fun GameScreen(gameState: GameState, modifier:Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1.0f)) {
            //matches
            Column {
                gameState.matches.forEach { CardView(it, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.0f)) }
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Column {
                gameState.nonMatches.forEach {
                    CardView(
                        it,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.0f)
                    )
                }
            }
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            maxItemsInEachRow = 3
        ) {
            gameState.hand.forEach { card ->
                CardView(
                    card, modifier = Modifier
                        .fillMaxWidth(0.28f)
                        .aspectRatio(1.0f),
                    onclick = {
                        Log.i("pressed hand", "$card")
                        gameState.playCard(card)
                    }
                )
            }
        }
    }

}


@Preview
@Composable
fun GameScreenPreview() {
    val gameState = GameState(Random(42))
    GameScreen(gameState)
}