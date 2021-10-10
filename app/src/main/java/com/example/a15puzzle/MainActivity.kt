package com.example.a15puzzle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val brain = GameBrain()

    private val handler: Handler = Handler(Looper.getMainLooper())

    private val run: Runnable = object : Runnable {
        override fun run() {
            findViewById<TextView>(R.id.textViewTimer).text = brain.getTime()
            brain.incrementTime()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        val gameJson = sharedPref.getString("state",null)
        if (gameJson != null) {
            brain.restoreGameFromJson(gameJson)
        } else {
            brain.newGame()
        }

        updateUI()
        startCounting()
    }

    override fun onResume() {
        startCounting()
        super.onResume()
        Log.d("Res", "Resume")
    }

    override fun onStop() {
        super.onStop()

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        with (sharedPref.edit()) {
            val jsonStr = brain.getGameJson()
            putString("state", jsonStr)
            commit()
        }

        handler.removeCallbacks(run)
    }

    fun gameBoardButtonOnClick(view: android.view.View) {
        val idStr = resources.getResourceEntryName(view.id)

        if (brain.validateMove(idStr)) {
            updateUI()
            if (brain.getWin()) {
                handler.removeCallbacks(run)
                findViewById<ImageView>(R.id.imageViewWin).visibility = View.VISIBLE
                Toast.makeText(applicationContext, "You won!", Toast.LENGTH_LONG).show()
            }
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
        findViewById<ImageView>(R.id.imageViewWin).visibility = View.INVISIBLE
        brain.newGame()
        updateUI()
        startCounting()
    }

    private fun startCounting() {
        handler.removeCallbacks(run)
        handler.post(run)
    }
}