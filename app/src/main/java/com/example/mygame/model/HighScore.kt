package com.example.mygame.model

data class HighScore private constructor(
    val score: Int,
    val date: Long,
    val longitude: Number,
    val latitude: Number
) {
    class Builder(
        var score: Int = 0,
        var date: Long = 0L,
        var longitude: Number = 0.0,
        var latitude: Number = 0.0
    ) {
        fun score(score: Int) = apply { this.score = score }
        fun date(date: Long) = apply { this.date = date }
        fun longitude(longitude: Number) = apply { this.longitude = longitude }
        fun latitude(latitude: Number) = apply { this.latitude = latitude }

        fun build() = HighScore(
            score, date, longitude, latitude
        )
    }
}
