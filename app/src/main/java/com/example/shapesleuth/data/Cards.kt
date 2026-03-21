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
    Red(Color(0xFFEF5350)),
    Orange(Color(0xFFFF9800)),
    Yellow(Color(0xFFFDD835)),
    Green(Color(0xFF66BB6A)),
    Blue(Color(0xFF42A5F5)),
    Purple(Color(0xFFAB47BC)),
    Brown(Color(0xFF8D6E63))
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
