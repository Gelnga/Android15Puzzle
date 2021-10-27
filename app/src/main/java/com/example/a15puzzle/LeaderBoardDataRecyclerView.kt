package com.example.a15puzzle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LeaderBoardDataRecyclerView(val context: Context, val repo: GameRepository) : RecyclerView.Adapter<LeaderBoardDataRecyclerView.ViewHolder>() {

    lateinit var dataSet: Array<String?>

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    fun refreshData() {
        dataSet = repo.getLeaderBoard()
    }

    init {
        refreshData()
    }

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = layoutInflater.inflate(R.layout.row_view, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leaderBoardData = dataSet[position]!!.split(";")

        val textViewPlayerName = holder.itemView.findViewById<TextView>(R.id.textViewPlayerName)
        textViewPlayerName.text = leaderBoardData[1]

        val textViewTimeSpent = holder.itemView.findViewById<TextView>(R.id.textViewTimeSpent)
        textViewTimeSpent.text = leaderBoardData[2]

        val textViewMovesMade = holder.itemView.findViewById<TextView>(R.id.textViewMovesMadeByPlayer)
        textViewMovesMade.text = leaderBoardData[3]

        val imageButtonDeleteLeader = holder.itemView.findViewById<ImageButton>(R.id.imageButtonDeleteLeader)
        imageButtonDeleteLeader.setOnClickListener {
            repo.deleteLeader(leaderBoardData[0])
            this.notifyItemRemoved(holder.layoutPosition)
            refreshData()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}