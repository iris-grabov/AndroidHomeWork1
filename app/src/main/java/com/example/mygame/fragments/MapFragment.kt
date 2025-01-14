package com.example.mygame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mygame.R
import com.example.mygame.model.HighScore
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.util.ArrayList

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    // List of all score locations
    private val scoreLocations: MutableList<LatLng> = mutableListOf()
    private var mapIsReady = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_map, container, false)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return v
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        mapIsReady = true

        // Enable zoom controls
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Focus on the first score or default to Israel
        if (scoreLocations.isNotEmpty()) {
//            focusNearFirstScore()
            addMarkersAndZoomToFit()
        } else {
            showDefaultIsraelMap()
        }
    }

    fun addScoreLocation(lat: Double, lon: Double) {
        // Add a new score location to the list
        val newLocation = LatLng(lat, lon)
        scoreLocations.add(newLocation)

        // If the map is already ready, update it to show the first score
        if (mapIsReady) {
//            focusNearFirstScore()
            addMarkersAndZoomToFit()
        }
    }

    fun addMarkersAndZoomToFit() {


        // Create a LatLngBounds.Builder to include all markers
        val boundsBuilder = LatLngBounds.Builder()

        // Add markers to the map
        scoreLocations.forEachIndexed { index, location ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Marker ${index + 1}")
            )
            boundsBuilder.include(location) // Include the marker's location in the bounds
        }

        // Build the LatLngBounds
        val bounds = boundsBuilder.build()

        // Set padding around the bounds (in pixels)
        val padding = 100 // Adjust as needed (e.g., 100 pixels)

        // Move and animate the camera to fit the bounds
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
    }


//    private fun focusNearFirstScore() {
//        if (scoreLocations.isNotEmpty()) {
//            val firstScoreLocation = scoreLocations[0]
//
//            // Move camera to first score with zoom level 10
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstScoreLocation, 10f))
//
//            // Add markers for all scores
//            for (location in scoreLocations) {
//                googleMap.addMarker(
//                    MarkerOptions()
//                        .position(location)
//                        .title("Score Location")
//                )
//            }
//        }
//    }

    private fun showDefaultIsraelMap() {
        // Default Israel location
        val israelLocation = LatLng(31.0461, 34.8516) // Central Israel
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israelLocation, 7f)) // Zoom level 7 for country view
    }

    fun zoom(lat: Double, lon: Double) {
        val location = LatLng(lat, lon)

        // Zoom to the location with a specific zoom level
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(location, 15f) // Adjust zoom level as needed
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Location Marker")
        )
    }

//    fun initialize(highScores: ArrayList<HighScore>) {
//        for (highScore in highScores) {
//            if (!(highScore.latitude == 0 && highScore.longitude == 0)) {
//                val location = LatLng(highScore.latitude.toDouble(), highScore.longitude.toDouble())
//                googleMap.addMarker(
//                    MarkerOptions()
//                        .position(location)
//                        .title("${highScore.score}")
//                )
//            }
//        }
//    }
}