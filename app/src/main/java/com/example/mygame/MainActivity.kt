package com.example.mygame

import TiltDetector
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mygame.logic.GameManager
import com.example.mygame.utilities.Constants
import com.example.mygame.utilities.SignalManager
import com.google.android.material.textview.MaterialTextView
import com.example.mygame.utilities.SingleSoundPlayer
import dev.tomco.a25a_10357_l07.interfaces.TiltCallback

import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var main_leftButton: AppCompatImageButton
    private lateinit var main_rightButton: AppCompatImageButton

    private lateinit var main_IMG_lifes: Array<AppCompatImageView>

    private lateinit var grid: LinearLayout

    private lateinit var gameManager: GameManager
    private lateinit var main_LBL_score: MaterialTextView

    private val totalRows = 9
    private val totalColumns = 5
    private var harryColumn = 1
    private lateinit var tiltDetector: TiltDetector


    private val handler = Handler(Looper.getMainLooper())

    private var obstacleCreationInterval = true // Default interval
    private var isButton= true // Default control mode

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Retrieve extras from Intent
        obstacleCreationInterval = intent.getBooleanExtra("SELECTED_SPEED", true)
        isButton = intent.getBooleanExtra("SELECTED_MODE" , true)

        SignalManager.init(this)
        initTiltDetector()

        findViews()
        gameManager= GameManager(main_IMG_lifes.size)
        initViews()
        showHarryImage(totalRows - 1, harryColumn)

        startLoop()

    }
    override fun onResume() {
        super.onResume()
        if (!isButton) {
            tiltDetector.start()
        } else {
            tiltDetector.stop()
        }
    }

    override fun onStop() {
        super.onStop()
        tiltDetector.stop()
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

        if(isButton) {
            main_leftButton.setOnClickListener {
                if (harryColumn > 0) {
                    hideImage(totalRows - 1, harryColumn)
                    harryColumn--
                    showHarryImage(totalRows - 1, harryColumn)
                }
            }

            main_rightButton.setOnClickListener {
                if (harryColumn < totalColumns - 1) {
                    hideImage(totalRows - 1, harryColumn)
                    harryColumn++
                    showHarryImage(totalRows - 1, harryColumn)
                }
            }
        }else{
            main_rightButton.visibility = View.INVISIBLE
            main_leftButton.visibility = View.INVISIBLE

        }
        for (i in 0 until totalRows) {
            for (j in 0 until totalColumns) {
                hideImage(i, j)
            }
        }

    }

    private fun startLoop() {
        handler.postDelayed(object : Runnable{
            override fun run() {
                val gameOver = refreshUI()
                if (!gameOver) {
                    handler.postDelayed(this, Constants.Timer.DELAY)
                }
            }
        }, Constants.Timer.DELAY)
    }


    private fun createObstacle() {
        val magicHatColumn = Random.nextInt(totalColumns)
        var coinColumn = magicHatColumn
        while (magicHatColumn == coinColumn) {
            coinColumn = Random.nextInt(totalColumns)
        }
        showMagicHatImage(0, magicHatColumn)
        showCoinImage(0, coinColumn)
    }

    private fun getCellImage(row: Int, column: Int): AppCompatImageView {
        val layout = grid.getChildAt(row) as LinearLayout
        return layout.getChildAt(column) as AppCompatImageView
    }

    private fun showMagicHatImage(row: Int, column: Int) {
        val image = getCellImage(row, column)
        image.setImageResource(R.drawable.magic_hat)
        image.setTag(R.drawable.magic_hat)
    }

    private fun showCoinImage(row: Int, column: Int) {
        val image = getCellImage(row, column)
        image.setImageResource(R.drawable.coin)
        image.setTag(R.drawable.coin)
    }

    private fun showHarryImage(row: Int, column: Int) {
        val image = getCellImage(row, column)
        image.setImageResource(R.drawable.harry_potter)
        image.setTag(R.drawable.harry_potter)
    }

    private fun hideImage(row: Int, column: Int) {
        val image = getCellImage(row, column)
        image.setImageResource(R.drawable.empty)
        image.setTag(R.drawable.empty)
    }

    private  fun refreshUI(): Boolean {
        updateObstacles()
        if (gameManager.isGameOver) {
            Log.d("Game Status", "Game Over! " + gameManager.score)
            changeActivity("Gryffindor:", gameManager.score)
            return true
        }

        if ( gameManager.shouldCreateObstacle(obstacleCreationInterval) ) {
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
                val drawableType = cell.getTag()
                if (drawableType != R.drawable.empty) {
                    if ((i == totalRows - 2) && (getCellImage(i+1, j).getTag() == R.drawable.harry_potter)) {
                        // last obstacle's row and there's a harry beneath
                        if (drawableType == R.drawable.magic_hat) {
                            toastAndVibrateAndSaund()
                            gameManager.collide()
                        } else {
                            gameManager.increaseScore()
                        }
                    } else {
                        if (i != totalRows - 2) {
                            // not last obstacle's row
                            if (drawableType == R.drawable.magic_hat) {
                                showMagicHatImage(i + 1, j)
                            } else {
                                showCoinImage(i + 1, j)
                            }
                        } else {
                            // last obstacle's row and there's no harry beneath
                            if (drawableType == R.drawable.magic_hat) {
                                gameManager.increaseScore()
                            }
                        }
                    }
                    hideImage(i, j)
                }
            }
        }
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltX(x: Float) {
                    val threshold = -1  // Define a sensitivity threshold for movement

                    // Move based on the tilt value
                    if (x < threshold && harryColumn < totalColumns - 1) {

                        hideImage(totalRows - 1, harryColumn)
                        harryColumn++
                        showHarryImage(totalRows - 1, harryColumn)
                    } else if (x > -threshold && harryColumn > 0) {
                        hideImage(totalRows - 1, harryColumn)
                        harryColumn--
                        showHarryImage(totalRows - 1, harryColumn)
                    }
                }

                override fun tiltY(y: Float) {
                    // Optionally handle vertical movement (up/down)
                    // For now, leave it empty if you're only handling horizontal tilt
                }
            }
        )
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

    private  fun toastAndVibrateAndSaund() {
        SignalManager.getInstance().toast("Slytherin!")
        SignalManager.getInstance().vibrate()
        var ssp: SingleSoundPlayer = SingleSoundPlayer(this)
        ssp.playSound(R.raw.boom)
    }
}