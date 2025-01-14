package com.example.mygame.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygame.R
import com.example.mygame.fragments.HighScoreFragment
import com.example.mygame.model.HighScore
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HighScoreAdapter(
    private val highScores: List<HighScore>,
    private val highScoreItemClicked: HighScoreFragment.Callback_HighScoreItemClicked?
) : RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder>() {

    // ViewHolder for each item
    inner class HighScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indexTextView: MaterialTextView = itemView.findViewById(R.id.score_index)
        val scoreTextView: MaterialTextView = itemView.findViewById(R.id.score_value)
        val dateTextView: MaterialTextView = itemView.findViewById(R.id.score_date)

        init {
            itemView.setOnClickListener {
                val score = highScores[adapterPosition]
                highScoreItemClicked?.highScoreItemClicked(score.latitude, score.longitude)
            }
        }


        fun bind(position: Int, highScore: HighScore) {
            // Set index based on position in the list
            indexTextView.text = (position + 1).toString()

            // Set the score and date
            scoreTextView.text = "Score: ${highScore.score}"

            // Convert milliseconds to a human-readable date
            val date = Date(highScore.date)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) // Format as per your requirement
            val formattedDate = dateFormat.format(date)

            // Set the formatted date
            dateTextView.text = "Date: $formattedDate"
        }
    }

    // Create a new ViewHolder when there is no existing ViewHolder to reuse
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.good_score, parent, false)
        return HighScoreViewHolder(itemView)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        val highScore = highScores[position]
        holder.bind(position, highScore)


    }

    // Return the size of the dataset
    override fun getItemCount(): Int {
        return highScores.size
    }
}

