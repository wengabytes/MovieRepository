package com.wengabytes.movieviewer.services.schedule

import com.google.gson.annotations.SerializedName

class GetScheduleRespModel(
    @SerializedName("dates")
    val listDate: List<ScheduleDateBean>,

    @SerializedName("cinemas")
    val listCinemaHolderBeans: List<CinemaHolderBean>,

    @SerializedName("times")
    val listTimeHolderBean: List<TimeHolderBean>
)