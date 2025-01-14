package com.example.mygame.fragments

//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.example.mygame.R
//import com.google.android.material.textview.MaterialTextView
//
//
//
//class MapFragment : Fragment() {
//
//
//    private lateinit var map_LBL_title: MaterialTextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val v = inflater.inflate(R.layout.fragment_map, container, false)
//        findViews(v)
//        return v
//    }
//
//    private fun findViews(v: View) {
//        map_LBL_title = v.findViewById(R.id.map_LBL_title)
//    }
//
//    fun zoom(lat: Double, lon: Double) {
//        map_LBL_title.text = buildString {
//            append("📍\n")
//            append(lat)
//            append(",\n")
//            append(lon)
//        }
//    }
//
//
//}

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mygame.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

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
    }

    fun zoom(lat: Double, lon: Double) {
        val location = LatLng(lat, lon)

        // Zoom to the location with a specific zoom level
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(location, 10f)  // Adjust zoom level as needed
        )

        googleMap.addMarker(MarkerOptions().position(location).title("Location Marker"))
    }
}
