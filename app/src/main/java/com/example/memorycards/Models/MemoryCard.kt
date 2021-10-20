package com.example.memorycards.Models

data class MemoryCard(
    val identifier : Int,
    var isFaceUp : Boolean = false,
    var isMatched : Boolean = false

)