package com.example.memorycards

import com.example.memorycards.Models.BoardSize
import com.example.memorycards.Models.MemoryCard

class MemoryGame (private val boardSize: BoardSize ) {


    val DEAFULT_ICONS = listOf<Int>(
        R.drawable.ic_bulb,
        R.drawable.ic_cake,
        R.drawable.ic_car,
        R.drawable.ic_cloud,
        R.drawable.ic_flower,
        R.drawable.ic_dollar,
        R.drawable.ic_leaf,
        R.drawable.ic_moon,
        R.drawable.ic_phone,
        R.drawable.ic_pool,
        R.drawable.ic_power,
        R.drawable.ic_scissor,
    )

    val cards :  List <MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard : Int? = null

    init {

        val choosenImages = DEAFULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (choosenImages + choosenImages).shuffled()
        cards =  randomizedImages.map { MemoryCard(it) }

    }

    fun flipCard(position: Int) : Boolean {
        numCardFlips++
        val card = cards[position]
        //There are 3 cases to consider...
        //0 cards previously flipped over => Flip over the selected card.
        //1 cards previously flipped over => Flip over the selected card & check if two cards match.
        //2 cards previously flipped over => Restore cards
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // Means, 0 || 2 cards previously flipped over.
            restoreTheCards()
            indexOfSingleSelectedCard = position
        }
        else {
            //1 card is flipped over.
                if (indexOfSingleSelectedCard != null){
                      checkForMatch(indexOfSingleSelectedCard!!,position)
                    indexOfSingleSelectedCard = null
                }
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int) : Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        } else {
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            numPairsFound++
            return true
        }

    }


    private fun restoreTheCards() {
        for( card in cards ) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardAlreadyFaceUp(position : Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2
    }
}