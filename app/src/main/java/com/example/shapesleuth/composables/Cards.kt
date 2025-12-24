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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode.Companion.Polygon
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center
        ) {
            Canvas(
                modifier = Modifier
                    .aspectRatio(1.0f)
                    .fillMaxSize()
                    .padding(4.dp)
                    .align(Alignment.CenterVertically),

                contentDescription = "Card:${card}"
            ) {
                val paint = SolidColor(card.color.color)
                when (card.shape) {
                    Shapes.Square -> drawRect(paint)
                    Shapes.Circle -> drawCircle(paint)
                    Shapes.Triangle -> drawPath(
                        brush = paint,
                        path = RoundedPolygon(
                            vertices = triangleVertices(size)
                        ).toPath().asComposePath()
                    )

                    Shapes.Diamond -> drawPath(
                        brush = paint,
                        path = RoundedPolygon(
                            vertices = diamondVertices(size)
                        ).toPath().asComposePath()
                    )

                    Shapes.Oval -> drawOval(
                        brush = paint,
                        topLeft = Offset(size.width / 8, 0f),
                        size = Size(3 * size.width / 4, size.height)
                    )

                    Shapes.Rainbow -> {
                        //drawPath(brush=paint,
//                        path = rainbowPolygon(size))
                        drawArc(
                            brush = paint,
                            startAngle = 0f,
                            sweepAngle = -180f,
                            useCenter = false,
                            style = Stroke(width =size.minDimension*.4f),
                            size = Size(size.width*.6f, size.height*.8f),
                            topLeft = Offset(size.width*.2f, size.height*.4f)

                        )
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
    val random = Random(7)
    val deck = makeDeck(random).shuffled(random) // Elway

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3)
    ) {
        items(deck) { card ->
            CardView(card, modifier = Modifier.height(128.dp))
        }
    }
}

fun triangleVertices(size: Size) = floatArrayOf(
    0F, size.height,
    size.width / 2, 0F,
    size.width, size.height
)

fun diamondVertices(size: Size) = floatArrayOf(
    0f, size.height / 2,
    size.width / 2, 0F,
    size.width, size.height / 2,
    size.width / 2, size.height
)

//unused
fun rainbowPolygon(size: Size): Path {
    val roundAmount = size.minDimension * .2f
    val sharpCorner = CornerRounding(0f)
    val rounded = CornerRounding(roundAmount)
    return RoundedPolygon(
        vertices = floatArrayOf(
            0f, size.height,
            size.width / 2, -roundAmount,
            size.width, size.height,
            .8f * size.width, size.height,
            size.width / 2, .2f * size.height - roundAmount,
            .2f * size.width, size.height
        ),
        perVertexRounding = listOf(
            sharpCorner,
            rounded,
            sharpCorner,
            sharpCorner,
            rounded,
            sharpCorner

        )
    ).toPath().asComposePath()
}