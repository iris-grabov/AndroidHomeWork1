package com.example.mygame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView
import com.example.mygame.utilities.Constants

class ScoreActivity : AppCompatActivity() {

    private lateinit var score_LBL_score: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        findViews()
        initViews()
    }

    private fun findViews() {
        score_LBL_score = findViewById(R.id.score_LBL_score)
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        val score = bundle?.getInt(Constants.BundleKeys.SCORE_KEY, 0)
        val message = bundle?.getString(Constants.BundleKeys.STATUS_KEY,"🤷🏻‍♂️Unknown")

        score_LBL_score.text = buildString {
            append(message)
            append("\n")
            append(score)
        }
    }

}