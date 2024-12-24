package com.example.mygame

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mygame.logic.GameManager
import com.example.mygame.utilities.Constants
import com.example.mygame.utilities.SignalManager
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var main_leftButton: AppCompatImageButton
    private lateinit var main_rightButton: AppCompatImageButton

    private lateinit var main_IMG_lifes: Array<AppCompatImageView>

    private lateinit var grid: LinearLayout

    private lateinit var gameManager: GameManager
    private lateinit var main_LBL_score: MaterialTextView

    private val totalRows = 6
    private val totalColumns = 3
    private var harryColumn = 1


    private val handler = Handler(Looper.getMainLooper())

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        SignalManager.init(this)
        findViews()
        gameManager= GameManager(main_IMG_lifes.size)
        initViews()
        showImage(totalRows - 1, harryColumn)
        startLoop()
    }

    private fun findViews() {
        main_leftButton = findViewById(R.id.left_arrow_button)
        main_rightButton = findViewById(R.id.right_arrow_button)
        grid = findViewById(R.id.grid_layout)
        main_LBL_score = findViewById(R.id.main_LBL_score)
        main_IMG_lifes = arrayOf(
            findViewById(R.id.main_IMG_life_1),
            findViewById(R.id.main_IMG_life_2),
            findViewById(R.id.main_IMG_life_3)
        )
    }

    private fun initViews() {
        main_LBL_score.text = gameManager.score.toString()
        main_leftButton.setOnClickListener {
            if (harryColumn > 0) {
                hideImage(totalRows - 1, harryColumn)
                harryColumn--
                showImage(totalRows - 1, harryColumn)
            }
        }

        main_rightButton.setOnClickListener {
            if (harryColumn < totalColumns - 1) {
                hideImage(totalRows - 1, harryColumn)
                harryColumn++
                showImage(totalRows - 1, harryColumn)
            }
        }
        for (i in 0 until totalRows) {
            for (j in 0 until totalColumns) {
                hideImage(i, j)
            }
        }

    }

//    private fun startLoop() {
//        handler.postDelayed(object : Runnable{
//            override private fun run() {
//                val gameOver = refreshUI()
//                if (!gameOver) {
//                    handler.postDelayed(this, 1000)
//                }
//            }
//        }, 1000)
//    }

    private fun startLoop() {
        lifecycleScope.launch {
            while (!gameManager.isGameOver) {
                refreshUI()
                delay(Constants.Timer.DELAY)
            }
        }
    }

    override fun onStop() {
        super.onStop()

    }

    private fun createObstacle() {
        val column = Random.nextInt(totalColumns)
        showImage(0, column)
    }

    private fun getCellImage(row: Int, column: Int): AppCompatImageView {
        val layout = grid.getChildAt(row) as LinearLayout
        return layout.getChildAt(column) as AppCompatImageView
    }

    private fun showImage(row: Int, column: Int) {
        val image = getCellImage(row, column)
        image.visibility = View.VISIBLE
    }

    private fun hideImage(row: Int, column: Int) {
        val image = getCellImage(row, column)
        image.visibility = View.INVISIBLE
    }

    private  fun refreshUI(): Boolean {
        updateObstacles()
        if (gameManager.isGameOver) {
            Log.d("Game Status", "Game Over! " + gameManager.score)
            changeActivity("Gryffindor:", gameManager.score)
            return true
        }

        if (gameManager.shouldCreateObstacle()) {
            createObstacle()
        }
        gameManager.tick()

        main_LBL_score.text = gameManager.score.toString()

        if (gameManager.collisions != 0) {
            main_IMG_lifes[main_IMG_lifes.size - gameManager.collisions].visibility = View.INVISIBLE
        }

        return false
    }

    private  fun updateObstacles() {
        for (i in totalRows - 2 downTo 0 ) {
            for (j in 0 until totalColumns) {
                val cell = getCellImage(i, j)
                if (cell.visibility == View.VISIBLE) {
                    if ((i == totalRows - 2) && (getCellImage(i+1, j).visibility == View.VISIBLE)) {
                        // last obstacle's row and there's a harry beneath
                        toastAndVibrate()
                        gameManager.collide()
                    } else {
                        if (i != totalRows - 2) {
                            // not last obstacle's row
                            showImage(i + 1, j)
                        } else {
                            // last obstacle's row and there's no harry beneath
                            gameManager.increaseScore()
                        }
                    }
                    hideImage(i, j)
                }
            }
        }
    }

    private  fun changeActivity(message: String, score: Int) {
        val intent = Intent(this, ScoreActivity::class.java)
        var bundle = Bundle()
        bundle.putInt(Constants.BundleKeys.SCORE_KEY, score)
        bundle.putString(Constants.BundleKeys.STATUS_KEY, message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private  fun toastAndVibrate() {
        SignalManager.getInstance().toast("Slytherin!")
        SignalManager.getInstance().vibrate()
    }
}