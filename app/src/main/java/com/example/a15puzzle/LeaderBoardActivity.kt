package com.example.a15puzzle

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class LeaderBoardActivity : AppCompatActivity() {

    private lateinit var repository: GameRepository
    private lateinit var recyclerViewLeaderBoardData: RecyclerView
    private lateinit var adapter: LeaderBoardDataRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        repository = GameRepository(applicationContext)
            .open()
        recyclerViewLeaderBoardData = findViewById(R.id.recyclerViewLeaderBoardData)

//        Generate random finished games. Used to test leaderboard activity

//        var x = 14
//        while (x > 0) {
//            repository.saveFinishedGame("Lasor", Random.nextInt(0, 1000), Random.nextInt(0, 1000).toString())
//            x--
//        }

        recyclerViewLeaderBoardData.layoutManager = LinearLayoutManager(this)
        adapter = LeaderBoardDataRecyclerView(this, repository)
        recyclerViewLeaderBoardData.adapter = adapter
    }

    fun finishActivity(view: View) {
        finish()
    }

    fun clearLeaderBoard(view: View) {
        repository.clearLeaderboard()
        Toast.makeText(applicationContext, "Leaderboard was cleared", Toast.LENGTH_SHORT).show()
        adapter.notifyItemRangeRemoved(0, adapter.itemCount)
        adapter.refreshData()
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.close()
    }
}