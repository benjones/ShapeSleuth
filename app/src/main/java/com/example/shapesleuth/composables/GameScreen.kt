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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shapesleuth.data.GameState
import com.example.shapesleuth.ui.theme.LightCoral
import com.example.shapesleuth.ui.theme.LightSeafoam
import kotlin.random.Random

@Composable
fun GameScreen(gameState: GameState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        val matches = gameState.matches
        val nonMatches = gameState.nonMatches
        val maxRows = 5

        // Use BoxWithConstraints to calculate available space for the board
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            val spacing = 4.dp
            val outerPadding = 8.dp
            val innerPadding = 8.dp

            // Determine columns needed per side (min 1 to maintain weight/layout)
            val mCols = maxOf(1, (matches.size + maxRows - 1) / maxRows)
            val nCols = maxOf(1, (nonMatches.size + maxRows - 1) / maxRows)
            val totalCols = mCols + nCols

            // Total spacing/padding overhead
            val totalPaddingWidth = outerPadding * 4 + innerPadding * 4
            val totalGapsWidth = spacing * (totalCols - 1)
            val totalPaddingHeight = outerPadding * 2 + innerPadding * 2
            val totalGapsHeight = spacing * (maxRows - 1)

            val availableWidth = maxWidth - totalPaddingWidth - totalGapsWidth
            val availableHeight = maxHeight - totalPaddingHeight - totalGapsHeight

            // Determine uniform card size that fits both height (5 rows) and total width
            val cardWidthSize = availableWidth / totalCols
            val cardHeightSize = availableHeight / maxRows
            val cardSize = if (cardWidthSize < cardHeightSize) cardWidthSize else cardHeightSize

            Row(modifier = Modifier.fillMaxSize()) {
                // matches (Left side)
                Column(
                    modifier = Modifier
                        .weight(mCols.toFloat())
                        .fillMaxHeight()
                        .padding(outerPadding)
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightSeafoam)
                        .padding(innerPadding)
                ) {
                    FlowColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        verticalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        matches.forEach { card ->
                            CardView(
                                card,
                                modifier = Modifier.size(cardSize)
                            )
                        }
                    }
                }

                // non-matches (Right side)
                Column(
                    modifier = Modifier
                        .weight(nCols.toFloat())
                        .fillMaxHeight()
                        .padding(outerPadding)
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightCoral)
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.End
                ) {
                    FlowColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.End),
                        verticalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        nonMatches.forEach { card ->
                            CardView(
                                card,
                                modifier = Modifier.size(cardSize)
                            )
                        }
                    }
                }
            }
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
