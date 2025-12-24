package com.example.shapesleuth.data

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

enum class Shapes {
    Circle,
    Square,
    Triangle,
    Oval,
    Diamond,
    Rainbow,
    Spiral
}

enum class Colors(val color: Color) {
    Red(Color.Red),
    Orange(Color(0xffff9900)),
    Yellow(Color.Yellow),
    Green(Color.Green),
    Blue(Color.Blue),
    Purple(Color(0xff9900ff)),
    Brown(Color(0xFF996633))
}

enum class Patterns {
    Solid,
    PolkaDot,
    Stripes,
    Stars,
    Plaid,
    Gradient,
    Waves
}

class Card(val shape: Shapes, val color: Colors, val pattern: Patterns)

fun makeDeck(random: Random = Random.Default): List<Card>{
    val patterns = Patterns.entries.toTypedArray()
    patterns.shuffle(random)
    return Shapes.entries.mapIndexed { i, shape ->
        Colors.entries.mapIndexed{ j, color ->
            Card(shape, color, patterns.get((i + j) % patterns.size))
        }
    }.flatten()
}