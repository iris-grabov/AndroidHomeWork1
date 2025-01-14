package com.example.mygame

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.mygame.fragments.HighScoreFragment
import com.example.mygame.fragments.MapFragment
import com.example.mygame.model.HighScore
import com.google.android.material.button.MaterialButton
import java.util.ArrayList

class TableScoreActivity : AppCompatActivity() {

    private lateinit var main_FRAME_list: FrameLayout
    private lateinit var main_FRAME_map: FrameLayout
    private lateinit var mapFragment: MapFragment
    private lateinit var highScoreFragment: HighScoreFragment
    private lateinit var goToMenuButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_score)

        findViews()
        initViews()
        setupButtonClickListener()
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
        goToMenuButton = findViewById(R.id.goToMenuButton) // Find the MaterialButton
    }

    private fun initViews() {
        mapFragment = MapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_map, mapFragment)
            .commit()

        highScoreFragment = HighScoreFragment()
        highScoreFragment.mapCallback = object :
            HighScoreFragment.MapCallback {
            override fun dataInitialized(highScores: ArrayList<HighScore>) {
                for (highScore in highScores) {
                    mapFragment.addScoreLocation(highScore.latitude.toDouble(), highScore.longitude.toDouble())
                }
            }

            override fun highScoreItemClicked(lat: Number, lon: Number) {
                mapFragment.zoom(lat.toDouble(), lon.toDouble())
            }
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_list, highScoreFragment)
            .commit()
    }

    private fun setupButtonClickListener() {
        goToMenuButton.setOnClickListener {
            // Navigate to MenuActivity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
