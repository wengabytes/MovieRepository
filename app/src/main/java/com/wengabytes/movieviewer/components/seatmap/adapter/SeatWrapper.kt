package com.wengabytes.movieviewer.components.seatmap.adapter

import com.wengabytes.movieviewer.R

class SeatWrapper(
    val id: String,
    val isAvailable: Boolean,
    var isSelected: Boolean = false
) : SeatWrapperInterface {
    override val viewType: Int = R.layout.i_seat
}