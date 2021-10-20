package com.example.memorycards

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorycards.Models.BoardSize
import com.example.memorycards.Models.MemoryCard
import com.google.android.material.snackbar.Snackbar
import nl.dionsegijn.konfetti.Confetti
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class MainActivity : AppCompatActivity() {

    private lateinit var clRoot: ConstraintLayout
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var rvBoard : RecyclerView
    private lateinit var tvNumMoves : TextView
    private lateinit var tvNumPairs : TextView
    private lateinit var viewKonfettiView: KonfettiView
    private val TAG : String = "Main Activity"

    private lateinit var memoryGame: MemoryGame
    private var boardSize : BoardSize = BoardSize.MEDIUM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        viewKonfettiView = findViewById(R.id.viewKonfetti)

        setupGame()

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId ) {
            R.id.mi_refresh -> { //setup game again.
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quit your current game?",null, View.OnClickListener {
                        setupGame()
                        tvNumMoves.text = "Moves : 0"
                    })
                }else{
                    setupGame()
                    tvNumMoves.text = "Moves : 0"
                }
            }
            R.id.mi_hardness_easy -> {
                boardSize = BoardSize.EASY
                setupGame()
            }
            R.id.mi_hardness_medium -> {
                boardSize = BoardSize.MEDIUM
                setupGame()
            }
            R.id.mi_hardness_hard -> {
                boardSize = BoardSize.HARD
                setupGame()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(title: String,view: View?,positiveClickListener : View.OnClickListener) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setView(view)
        alert.setNegativeButton("Cancel",null)
        alert.setPositiveButton("OK") {_,_ ->
            positiveClickListener.onClick(null)
        }.show()

    }

    private fun setupGame() {

        memoryGame = MemoryGame(boardSize)
        when (boardSize) {
            BoardSize.EASY -> {
                tvNumPairs.setTextColor(resources.getColor(R.color.design_default_color_error))
                tvNumPairs.text = "Pairs: 0/3"
                tvNumMoves.text = "Moves: 0"
            }
            BoardSize.MEDIUM -> {
                tvNumPairs.setTextColor(resources.getColor(R.color.design_default_color_error))
                tvNumPairs.text = "Pairs: 0/6"
                tvNumMoves.text = "Moves: 0"
            }
            BoardSize.HARD -> {
                tvNumPairs.setTextColor(resources.getColor(R.color.design_default_color_error))
                tvNumPairs.text = "Pairs: 0/10"
                tvNumMoves.text = "Moves: 0"
            }
        }

        adapter = MemoryBoardAdapter(this, boardSize,memoryGame.cards, object : MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }


        })

        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())

    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame()) {
            //Alert user.
                Snackbar.make(clRoot,"You already won !",Snackbar.LENGTH_SHORT).show()
            return
        }

        if (memoryGame.isCardAlreadyFaceUp(position)) {
            //Alert user.
            Snackbar.make(clRoot,"Invalid move !",Snackbar.LENGTH_LONG).show()
            return
        }

        // Actually Flip Over the Card
        memoryGame.flipCard(position)

            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
        if(memoryGame.numPairsFound == boardSize.getNumPairs()) {
            tvNumPairs.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        } else {
            tvNumPairs.setTextColor(resources.getColor(R.color.design_default_color_error))
        }


            if (memoryGame.haveWonGame()) {
                Snackbar.make(clRoot,"You WON! Congrats!",Snackbar.LENGTH_SHORT).show()
                viewKonfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.Square, Shape.Circle)
                    .addSizes(Size(12))
                    .setPosition(-50f, viewKonfettiView.width + 50f, -50f, -50f)
                    .streamFor(300, 5000L)

            }
        tvNumMoves.text = "Moves : ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
        }

    }
