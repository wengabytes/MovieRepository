package com.wengabytes.movieviewer.services.schedule

import com.google.gson.annotations.SerializedName

class ScheduleDateBean(
    @SerializedName("id")
    val id: String,

    @SerializedName("label")
    val label: String,

    @SerializedName("date")
    val date: String
)