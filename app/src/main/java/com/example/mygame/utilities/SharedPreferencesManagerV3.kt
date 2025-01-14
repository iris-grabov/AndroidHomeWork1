package com.example.mygame.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManagerV3 private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.SP_keys.HIGH_SCORE_KEY,
        Context.MODE_PRIVATE
    )

    companion object {
        @Volatile
        private var instance: SharedPreferencesManagerV3? = null

        fun init(context: Context): SharedPreferencesManagerV3 {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManagerV3(context).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManagerV3 {
            return instance ?: throw IllegalStateException(
                "SharedPreferencesManagerV3 must be initialized by calling init(context) before use."
            )
        }
    }


    fun putString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(
            key, defaultValue
        ) ?: defaultValue
    }

}