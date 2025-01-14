package com.example.mygame

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mygame.logic.GameManager
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    private lateinit var MENU_BTN_FAST: MaterialButton
    private lateinit var MENU_BTN_SLOW: MaterialButton
    private lateinit var MENU_BTN_RECORDS: MaterialButton
    private lateinit var MENU_BTN_PLAY: MaterialButton
    private lateinit var MENU_BTN_SENSOR: MaterialButton
    private lateinit var MENU_BTN_BUTTONS: MaterialButton

    private var isFastOrSlowClicked = false
    private var isFast = false
    private var isButton = true
    private var isSensorOrButtonsClicked = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        findViews()
        initViews()
    }

    private fun findViews() {
        MENU_BTN_FAST = findViewById(R.id.BTN_fast)
        MENU_BTN_SLOW = findViewById(R.id.BTN_slow)
        MENU_BTN_RECORDS = findViewById(R.id.BTN_record)
        MENU_BTN_PLAY = findViewById(R.id.BTN_play)
        MENU_BTN_SENSOR = findViewById(R.id.sensors_BTN)
        MENU_BTN_BUTTONS = findViewById(R.id.BTN_original)
    }

    private fun initViews() {
        MENU_BTN_FAST.setOnClickListener { view: View -> buttonClicked("fast") }
        MENU_BTN_SLOW.setOnClickListener { view: View -> buttonClicked("slow") }
        MENU_BTN_RECORDS.setOnClickListener { view: View -> buttonClicked("records") }
        MENU_BTN_PLAY.setOnClickListener { view: View -> startMainActivityIfReady() }
        MENU_BTN_SENSOR.setOnClickListener { view: View -> buttonClicked("sensor") }
        MENU_BTN_BUTTONS.setOnClickListener { view: View -> buttonClicked("buttons") }
    }

    private fun buttonClicked(btn: String) {
        when (btn) {
            "fast" -> {
                MENU_BTN_FAST.setBackgroundColor(Color.YELLOW)
                MENU_BTN_SLOW.setBackgroundColor(Color.WHITE)
                isFastOrSlowClicked = true
                isFast=true
            }
            "slow" -> {
                MENU_BTN_SLOW.setBackgroundColor(Color.YELLOW)
                MENU_BTN_FAST.setBackgroundColor(Color.WHITE)
                isFastOrSlowClicked = true
                isFast=false
            }
            "sensor" -> {
                MENU_BTN_SENSOR.setBackgroundColor(Color.YELLOW)
                MENU_BTN_BUTTONS.setBackgroundColor(Color.WHITE)
                isSensorOrButtonsClicked = true
                isButton=false
            }
            "buttons" -> {
                MENU_BTN_BUTTONS.setBackgroundColor(Color.YELLOW)
                MENU_BTN_SENSOR.setBackgroundColor(Color.WHITE)
                isSensorOrButtonsClicked = true
                isButton=true
            }
            "records" -> {
                changeActivity(TableScoreActivity::class.java)
            }
            else -> {
                Toast.makeText(this, "Unknown action: $btn", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMainActivityIfReady() {
        if (isFastOrSlowClicked && isSensorOrButtonsClicked) {
            val intent = Intent(this, MainActivity::class.java)
            // Add extras to the intent
            intent.putExtra("SELECTED_SPEED", isFast)
            intent.putExtra("SELECTED_MODE", isButton)

            // Start the activity
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please select both speed and mode first.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun changeActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()
    }
}
