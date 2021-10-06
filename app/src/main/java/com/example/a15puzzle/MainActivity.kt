package com.example.a15puzzle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

val brain = GameBrain()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        // test
        val boardJson = sharedPref.getString("state",null)
        if (boardJson != null) {
            brain.restoreBoardFromJson(boardJson)
        } else {
            brain.newGame()
        }

        updateUI()
    }

    override fun onStop() {
        super.onStop()

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        with (sharedPref.edit()) {
            val jsonStr = brain.getBoardJson()
            putString("state", jsonStr)
            commit()
        }
    }

    fun gameBoardButtonOnClick(view: android.view.View) {
        val idStr = resources.getResourceEntryName(view.id)

        if (brain.validateMove(idStr)) {
            updateUI()
        }
    }

    private fun updateUI() {
        for (x in brain.getGameBoard().indices) {
            for (y in brain.getGameBoard().indices) {

                val id = resources.getIdentifier("imageGameButton$x$y", "id", packageName)
                val button = findViewById<ImageButton>(id)
                if (brain.getGameBoard()[x][y] == "X") {
                    button.setImageDrawable(null)
                    continue
                }

                val xImageCord = brain.getGameBoard()[x][y][0]
                val yImageCord = brain.getGameBoard()[x][y][1]

                val imageId = resources.getIdentifier("puzzle$xImageCord$yImageCord", "drawable", packageName)
                button.setImageDrawable(ResourcesCompat.getDrawable(resources, imageId, null))
            }
        }
        val movesText = findViewById<TextView>(R.id.textViewMovesMade)
        movesText.text = brain.getMoves().toString()
    }

    fun undo(view: android.view.View) {
        brain.undo()
        updateUI()
    }

    fun newGame(view: android.view.View) {
        brain.newGame()
        updateUI()
    }
}