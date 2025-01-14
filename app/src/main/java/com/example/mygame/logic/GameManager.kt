package com.example.mygame.logic

import com.example.mygame.utilities.Constants

class GameManager (private val lifeCount: Int = 3) {

    var score: Int = 0
        private set

    var slow: Int = 8
        private set

    var fast: Int = 2
        private set



    var collisions: Int = 0
        private set

    var count: Int = 0
        private set



    val isGameOver: Boolean
        get() = collisions == lifeCount

    fun shouldCreateObstacle(obstacleCreationInterval: Boolean): Boolean {
        val frequency = if (obstacleCreationInterval) fast else slow // Fewer ticks for fast mode
        return count % frequency == 0
    }



    fun collide() {
        collisions++
    }

    fun tick() {
        count++
    }

    fun increaseScore() {
        score += Constants.GameLogic.ANSWER_POINTS
    }
}