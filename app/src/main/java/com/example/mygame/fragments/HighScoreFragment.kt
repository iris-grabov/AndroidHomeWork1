package com.example.mygame.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygame.R
import com.example.mygame.adapters.HighScoreAdapter
import com.example.mygame.model.HighScore
import com.example.mygame.utilities.Constants
import com.example.mygame.utilities.SharedPreferencesManagerV3
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

//import dev.tomco.a25a_10357_l06.interfaces.Callback_HighScoreItemClicked

class HighScoreFragment : Fragment() {

    private lateinit var highScore_ET_location: TextInputEditText

//    private lateinit var highScore_BTN_send: MaterialButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var noRecordsTextView: MaterialTextView

    var mapCallback: MapCallback? = null

    public interface MapCallback {
        fun dataInitialized(highScores: ArrayList<HighScore>)
        fun highScoreItemClicked(lat: Number, lon: Number)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(v)
        initViews(v)
        return v
    }


    private fun initViews(v: View) {
//        highScore_BTN_send.setOnClickListener { v: View ->
//            var coordinates = highScore_ET_location.text?.split(",")
//            var lat: Double = coordinates?.get(0)?.toDouble() ?: 0.0
//            var lon: Double = coordinates?.get(1)?.toDouble() ?: 0.0
//
//            itemClicked(lat, lon)
//        }

        val gson = Gson()

        val highScoresListString = SharedPreferencesManagerV3.getInstance().getString(
            Constants.SP_keys.HIGH_SCORE_KEY, "[]"
        )

        val itemType = object : TypeToken<ArrayList<HighScore>>() {}.type
        val highScores = gson.fromJson<ArrayList<HighScore>>(highScoresListString, itemType)

        if (highScores.isEmpty()) {
            noRecordsTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this.activity)
            val adapter = HighScoreAdapter(highScores, mapCallback)
            recyclerView.adapter = adapter
            mapCallback?.dataInitialized(highScores)
        }
    }

    private fun itemClicked(lat: Double, lon: Double) {
//        highScoreItemClicked?.highScoreItemClicked(lat,lon)
    }

    private fun findViews(v: View) {
//        highScore_ET_location = v.findViewById(R.id.highScore_ET_location)
//        highScore_BTN_send = v.findViewById(R.id.highScore_BTN_send)
        recyclerView = v.findViewById(R.id.main_RV_list)
        noRecordsTextView = v.findViewById(R.id.no_records_textview)
    }
}