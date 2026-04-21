package com.example.shapesleuth.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.shapesleuth.data.Card
import com.example.shapesleuth.data.GameState
import com.example.shapesleuth.data.makeDeck
import com.example.shapesleuth.ui.theme.LightCoral
import com.example.shapesleuth.ui.theme.LightSeafoam
import kotlin.random.Random

@Composable
fun GameScreen(gameState: GameState, modifier:Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(1.0f)) {
            // matches
            FlowColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(LightSeafoam),
                horizontalArrangement = Arrangement.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachColumn = 5,
            ) {
                gameState.matches.forEach { card ->
                    CardView(
                        card,
                        modifier = Modifier
                            .widthIn(max=96.dp)
                            .weight(1.0f)
                            .aspectRatio(1.0f)

                    )
                }
            }

            // non-matches
            FlowColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(LightCoral),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.End,
                maxItemsInEachColumn = 5
            ) {
                gameState.nonMatches.forEach { card ->
                    CardView(
                        card,
                        modifier = Modifier
                            .widthIn(max=96.dp)
                            .weight(1.0f)
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
    for(i in 1..16) {
        gameState.playCard(gameState.hand.last())
    }
    GameScreen(gameState)
}