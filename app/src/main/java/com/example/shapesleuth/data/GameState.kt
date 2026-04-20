package com.example.shapesleuth.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import kotlin.random.Random

class GameState(random: Random = Random.Default) {
    private val _hiddenCard: Card
    val hiddenCard: Card get() = _hiddenCard
    private val _deck = mutableStateListOf<Card>()
    val deck: List<Card> get() = _deck

    private val _hand = mutableStateListOf<Card>()
    val hand: List<Card> get() = _hand

    private val _matches = mutableStateListOf<Card>()
    val matches: List<Card> get() = _matches

    private val _nonMatches = mutableStateListOf<Card>()
    val nonMatches: List<Card> get() = _nonMatches

    init {
        val fullDeck = makeDeck(random).shuffled(random)
        _hiddenCard = fullDeck.first()
        _hand.addAll(fullDeck.subList(1, 6))
        _deck.addAll(fullDeck.drop(6))
    }

    fun playCard(card : Card){

        _hand.remove(card)
        if(card.color == _hiddenCard.color ||
            card.shape == _hiddenCard.shape ||
            card.pattern == _hiddenCard.pattern){
            _matches.add(card)
        } else {
            _nonMatches.add(card)
        }
        _hand.add(_deck.removeLast())
    }

}
