package com.example.mygame

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.mygame.fragments.HighScoreFragment
import com.example.mygame.fragments.MapFragment

//import dev.tomco.a25a_10357_l06.interfaces.Callback_HighScoreItemClicked

class TableScoreActivity : AppCompatActivity() {


    private lateinit var main_FRAME_list: FrameLayout

    private lateinit var main_FRAME_map: FrameLayout

    private lateinit var mapFragment: MapFragment

    private lateinit var highScoreFragment: HighScoreFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_score)

        findViews()
        initViews()
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
    }

    private fun initViews() {
        mapFragment = MapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_map ,mapFragment )
            .commit()

        highScoreFragment = HighScoreFragment()
        highScoreFragment.highScoreItemClicked = object :
            HighScoreFragment.Callback_HighScoreItemClicked {
            override fun highScoreItemClicked(lat: Number, lon: Number) {
                mapFragment.zoom(lat.toDouble(),lon.toDouble())
            }
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_list ,highScoreFragment )
            .commit()

    }
}