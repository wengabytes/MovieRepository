package com.wengabytes.movieviewer.services.schedule

import com.google.gson.annotations.SerializedName

class TimeHolderBean(
    @SerializedName("parent")
    val parent: String,

    @SerializedName("times")
    val listTimeBeans: List<TimeBean>
)

class TimeBean(
    @SerializedName("id")
    val id: String,

    @SerializedName("label")
    val label: String,

    @SerializedName("schedule_id")
    val scheduleId: String,

    @SerializedName("popcorn_price")
    val popcornPrice: String,

    @SerializedName("popcorn_label")
    val popcornLabel: String,

    @SerializedName("seating_type")
    val seatingType: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("variant")
    val variant: String?
)
