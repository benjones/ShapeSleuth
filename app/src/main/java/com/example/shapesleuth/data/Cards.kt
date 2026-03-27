package com.example.shapesleuth.data

import androidx.compose.ui.graphics.Color
import com.example.shapesleuth.ui.theme.*
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
    Red(Red400),
    Orange(Orange500),
    Yellow(Yellow600),
    Green(Green400),
    Blue(Blue400),
    Purple(Purple400),
    Brown(Brown400)
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
