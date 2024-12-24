package com.example.mygame.logic

import com.example.mygame.utilities.Constants

class GameManager (private val lifeCount: Int = 3) {

    var score: Int = 0
        private set

    var collisions: Int = 0
        private set

    var count: Int = 0
        private set

    val isGameOver: Boolean
        get() = collisions == lifeCount

    fun shouldCreateObstacle(): Boolean {
        return count % 5 == 0
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