package com.example.memorycards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.memorycards.Models.BoardSize
import com.example.memorycards.Models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener : CardClickListener

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val MARGIN_SIZE = 15

    }

    interface CardClickListener{
        fun onCardClicked (position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cardWidth : Int = parent.width / boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight : Int = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength = min(cardWidth,cardHeight)

        val view = LayoutInflater.from(context).inflate(R.layout.memory_card,parent,false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = cardSideLength
        layoutParams.width = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }


    override fun getItemCount() = boardSize.numCards


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.bind (position)
    }

     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        }

    private fun RecyclerView.ViewHolder.bind(position: Int) {
        val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        val memoryCard = cards[position]
        imageButton.setImageResource(if (memoryCard.isFaceUp) memoryCard.identifier else R.drawable.ic_baseline_help_24)

        imageButton.alpha = if (memoryCard.isMatched) .4f else 1.0f
        val colorStateList = if (memoryCard.isMatched) ContextCompat.getColorStateList(context, R.color.teal_700) else null
        ViewCompat.setBackgroundTintList(imageButton,colorStateList)
        imageButton.setOnClickListener{
            println("Clicked on position $position")
            cardClickListener.onCardClicked(position)
        }
    }
    }











