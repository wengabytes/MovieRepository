package com.wengabytes.movieviewer.services.seatmap

import com.google.gson.annotations.SerializedName

class GetSeatmapRespModel(
    @SerializedName("seatmap")
    val listSeatmaps: List<List<String>>,

    @SerializedName("available")
    val availableSeats: AvailableSeats
)

class AvailableSeats(
    @SerializedName("seat_count")
    val seatCount: Int,

    @SerializedName("seats")
    val listSeats: List<String>
)