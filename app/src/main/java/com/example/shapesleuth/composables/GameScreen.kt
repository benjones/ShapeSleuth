package com.example.shapesleuth.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.shapesleuth.data.Card
import com.example.shapesleuth.data.GameState
import com.example.shapesleuth.ui.theme.LightCoral
import com.example.shapesleuth.ui.theme.LightSeafoam
import kotlin.random.Random

@Composable
fun BoardArea(
    cards: List<Card>,
    backgroundColor: Color,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    val maxSize = 128.dp
    val spacing = 4.dp
    val innerPadding = 8.dp

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(innerPadding)
    ) {

        var cols = 1
        var rows = 1
        var cardSize = maxSize
        //to fit stuff in 1 col, grow if necessary
        while(true){
            // Calculate available space
            val totalGapsWidth = spacing * (cols - 1)
            val availableWidth = maxWidth - (innerPadding * 2) - totalGapsWidth
            val cardWidthSize = availableWidth / cols
            cardSize = min(maxSize, cardWidthSize)

            //FIXME padding?
            rows = ((maxHeight - (innerPadding*2))/(cardSize)).toInt()

            if(cards.size <= rows*cols){
                break;
            }
            cols += 1

        }

        FlowColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(spacing, alignment),
            verticalArrangement = Arrangement.spacedBy(spacing),
            maxItemsInEachColumn = rows
        ) {
            cards.forEach { card ->
                CardView(
                    card,
                    modifier = Modifier.size(cardSize)
                )
            }
        }
    }
}

@Composable
fun GameScreen(gameState: GameState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        val matches = gameState.matches
        val nonMatches = gameState.nonMatches
        val maxRows = 5

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .padding(8.dp)
        ) {
            // Determine columns needed per side for weight distribution
            val mCols = maxOf(1, (matches.size + maxRows - 1) / maxRows)
            val nCols = maxOf(1, (nonMatches.size + maxRows - 1) / maxRows)

            BoardArea(
                cards = matches,
                backgroundColor = LightSeafoam,
                alignment = Alignment.Start,
                modifier = Modifier
                    .weight(mCols.toFloat())
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )

            BoardArea(
                cards = nonMatches,
                backgroundColor = LightCoral,
                alignment = Alignment.End,
                modifier = Modifier
                    .weight(nCols.toFloat())
                    .fillMaxHeight()
                    .padding(start = 4.dp)
            )
        }

        // Player Hand
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
    repeat(16) {
        if (gameState.hand.isNotEmpty()) {
            gameState.playCard(gameState.hand.last())
        }
    }
    GameScreen(gameState)
}
