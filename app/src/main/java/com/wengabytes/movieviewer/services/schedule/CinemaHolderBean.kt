package com.wengabytes.movieviewer.services.schedule

import com.google.gson.annotations.SerializedName

class CinemaHolderBean(
    @SerializedName("parent")
    val parent: String,

    @SerializedName("cinemas")
    val listCinemaBeans: List<CinemaBean>
)

class CinemaBean(
    @SerializedName("id")
    val id: String,

    @SerializedName("cinema_id")
    val cinemaId: String,

    @SerializedName("label")
    val label: String
)