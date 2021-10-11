package com.example.a15puzzle

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class LeaderBoardActivity : AppCompatActivity() {

    private lateinit var repository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        repository = GameRepository(applicationContext)
        createTable()
    }

    private fun createTable() {
        val tl = findViewById<TableLayout>(R.id.tableLayoutLeaderboad)

        repository.open()
        val leaderboard = repository.getLeaderBoard()
        repository.close()

        for (game in leaderboard) {
            val splitGame = game!!.split(";")

            val tableRow = TableRow(this)
            tableRow.setBackgroundColor(ContextCompat
                .getColor(applicationContext, R.color.leaderbordRowBackground))
            tableRow.setPadding(2)
            tableRow.weightSum = 7.toFloat()
            tableRow.gravity = Gravity.CENTER
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
            )

            val nickNames = TextView(this)
            nickNames.text = splitGame[0]
            nickNames.textSize = 20.toFloat()
            nickNames.setTextColor(Color.BLACK)
            nickNames.gravity = Gravity.CENTER
            nickNames.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                2f
            )
            tableRow.addView(nickNames)


            val movesMade = TextView(this)
            movesMade.text = splitGame[1] + " Sec."
            movesMade.textSize = 20.toFloat()
            movesMade.setTextColor(Color.BLACK)
            movesMade.gravity = Gravity.CENTER
            movesMade.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                2f
            )
            tableRow.addView(movesMade)

            val timeSpent = TextView(this)
            timeSpent.text = splitGame[2]
            timeSpent.textSize = 20.toFloat()
            timeSpent.setTextColor(Color.BLACK)
            timeSpent.gravity = Gravity.CENTER
            timeSpent.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                2f
            )
            tableRow.addView(timeSpent)

            tl.addView(tableRow, TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,  //part4
                    TableLayout.LayoutParams.WRAP_CONTENT,
                )
            )
        }
    }

    fun finishActivity(view: View) {
        finish()
    }

    fun clearLeaderBoard(view: View) {
        repository.open()
        repository.clearLeaderboard()
        Toast.makeText(applicationContext, "Leaderboard was cleared", Toast.LENGTH_SHORT).show()
        repository.close()
        clearTable()
    }

    private fun clearTable() {
        val tl = findViewById<TableLayout>(R.id.tableLayoutLeaderboad)

        val childCount = tl.childCount

        if (childCount > 1) {
            tl.removeViews(1, childCount - 1)
        }
    }
}