package com.example.mygame.utilities

import com.example.mygame.model.HighScore
import java.util.ArrayList

object DataManager {
    fun addNewScore(highScores: ArrayList<HighScore>, score: Int?, location: Pair<Double, Double>?): ArrayList<HighScore> {
        val highScore = HighScore.Builder()
            .score(score ?: 0)
            .date(System.currentTimeMillis())
            .latitude(location?.first ?: 0)
            .longitude(location?.second ?: 0)
            .build()

        highScores.add(highScore)

        highScores.sortWith(Comparator { o1, o2 -> if (o1.score < o2.score) 1 else -1  })

        if (highScores.size > 10) {
            highScores.removeAt(10)
        }

        return highScores
    }
}