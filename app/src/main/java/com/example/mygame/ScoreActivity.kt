package com.example.mygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mygame.model.HighScore
import com.example.mygame.utilities.Constants
import com.example.mygame.utilities.DataManager
import com.example.mygame.utilities.SharedPreferencesManagerV3
import com.google.android.material.textview.MaterialTextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class ScoreActivity : AppCompatActivity() {

    private lateinit var score_LBL_score: MaterialTextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check if the location permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            // If permission is granted, fetch the location
            getLastLocation()
        }

        findViews()
        initViews()

        // Delay the transition to TableScoreActivity by 1 second
        Handler().postDelayed({
            val intent = Intent(this, TableScoreActivity::class.java)
            startActivity(intent)
            finish() // Close the current activity to prevent going back
        }, 1000) // 1000 milliseconds = 1 second
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

    private fun saveHighScore(location: Pair<Double, Double>?) {
        val gson = Gson()

        val highScoresListString = SharedPreferencesManagerV3.getInstance().getString(
            Constants.SP_keys.HIGH_SCORE_KEY, "[]"
        )

        val itemType = object : TypeToken<ArrayList<HighScore>>() {}.type
        val highScores = gson.fromJson<ArrayList<HighScore>>(highScoresListString, itemType)

        val bundle: Bundle? = intent.extras
        val score = bundle?.getInt(Constants.BundleKeys.SCORE_KEY, 0)
        val updatedHighScores = DataManager.addNewScore(highScores, score, location)

        val updatedHighScoresString = gson.toJson(updatedHighScores)

        SharedPreferencesManagerV3.getInstance().putString(Constants.SP_keys.HIGH_SCORE_KEY, updatedHighScoresString)
    }

    // Handle the permission result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, get the location
            getLastLocation()
        } else {
            // Permission denied
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            saveHighScore(null)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                saveHighScore(Pair(latitude, longitude))
                // Use the latitude and longitude as needed
                Toast.makeText(this, "Location: $latitude, $longitude", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
                saveHighScore(null)
            }
        }
    }
}
