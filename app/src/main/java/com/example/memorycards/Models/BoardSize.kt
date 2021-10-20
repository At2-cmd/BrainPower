package com.example.memorycards.Models

enum class BoardSize( val numCards : Int) {
    EASY(6),
    MEDIUM(12),
    HARD(20);

    fun getWidth() : Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }
    fun getHeight() : Int {
        return numCards / getWidth()
    }

    fun getNumPairs() : Int {
        return numCards / 2
    }
}