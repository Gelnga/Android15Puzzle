package com.example.a15puzzle

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity() {
    private val brain = GameBrain()
    private lateinit var repository: GameRepository

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
        repository = GameRepository(applicationContext)
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
        if (brain.getWin()) {
            findViewById<ImageView>(R.id.imageViewWin).visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        startCounting()
        if (brain.getWin()) {
            handler.removeCallbacks(run)
        }
        super.onResume()
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

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Enter your name")
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)

                var playerName: String
                builder.setPositiveButton("OK") { _, _ ->
                    repository.open()
                    playerName = input.text.toString()
                    val gameData = brain.getGameJson().split(";")
                    repository.saveFinishedGame(playerName, gameData[3].toInt(), gameData[4])
                    repository.close()
                }

                builder.show()
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
        var textViewPlaceholder = findViewById<TextView>(R.id.textViewMovesMade)
        textViewPlaceholder.text = brain.getMoves().toString()
        textViewPlaceholder = findViewById(R.id.textViewTimer)
        textViewPlaceholder.text = brain.getTime()
    }

    fun undo(view: View) {
        brain.undo()
        updateUI()
    }

    fun newGame(view: View) {
        findViewById<ImageView>(R.id.imageViewWin).visibility = View.INVISIBLE
        brain.newGame()
        updateUI()
        startCounting()
    }

    fun saveGame(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter save name")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        var gameName: String
        builder.setPositiveButton("OK") { _, _ ->
            repository.open()
            gameName = input.text.toString()
            repository.saveUnfinishedGame(gameName, brain.getGameJson())
            repository.close()}

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    fun loadGame(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose saved game")

        repository.open()
        val saves = repository.getSavedGames()
        repository.close()

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
        for (save in saves.keys) {
            arrayAdapter.add(save)
        }

        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.setAdapter(arrayAdapter) { _, which ->
            val data = arrayAdapter.getItem(which)
            saves[data]?.let { brain.restoreGameFromJson(it) }
            findViewById<ImageView>(R.id.imageViewWin).visibility = View.INVISIBLE
            startCounting()
            updateUI()
        }
        builder.show()

    }

    fun deleteSaves(view: View) {
        repository.open()
        repository.deleteSaves()
        Toast.makeText(applicationContext, "Saves were deleted", Toast.LENGTH_SHORT).show()
        repository.close()
    }

    fun showLeaderboard(view: View) {
        val showLeaderboard = Intent(this, LeaderBoardActivity::class.java)
        startActivity(showLeaderboard)
    }

    private fun startCounting() {
        handler.removeCallbacks(run)
        handler.post(run)
    }
}