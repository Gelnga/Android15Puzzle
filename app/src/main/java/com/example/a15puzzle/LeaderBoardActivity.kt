package com.example.a15puzzle

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;24;73;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;56;58;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;12;95;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;215;78;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;35;45;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;74;124;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;257;245;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;31;109;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;64;33;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;73;25;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;32;30;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;73;15;true")
        repository.saveFinishedGame("Laros", "[[\"00\",\"01\",\"02\",\"03\"],[\"10\",\"11\",\"12\",\"13\"],[\"20\",\"21\",\"22\",\"23\"],[\"30\",\"31\",\"32\",\"X\"]];[\"imageGameButton33\",\"imageGameButton23\"];imageGameButton33;12;20;true")

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