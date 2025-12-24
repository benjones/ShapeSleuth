package com.example.shapesleuth

import com.example.shapesleuth.data.Card
import com.example.shapesleuth.data.Colors
import com.example.shapesleuth.data.Shapes
import com.example.shapesleuth.data.makeDeck
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CardsTest {
    @Test
    fun `test make deck`() {
        for(i in 1..100) {
            val deck = makeDeck()
            validateDeck(deck)
        }
    }

    fun validateDeck(deck: List<Card>){
        assertEquals( "Wrong number of cards in deck",
            Shapes.entries.size* Colors.entries.size,
            deck.size)

        //make sure exactly 0 or 1 feature of each cards matches
        deck.forEachIndexed { i,  card ->
            deck.drop(i +1).forEachIndexed { j, card2 ->
                //0 or 1 features can match
                assertTrue(listOf(card.shape == card2.shape,
                    card.pattern == card2.pattern,
                    card.color == card2.color)
                    .map{ if(it) 1 else 0}
                    .sum() <= 1)
            }
        }
    }
}