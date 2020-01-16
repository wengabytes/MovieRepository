package com.wengabytes.movieviewer.components.seatmap.adapter

import com.wengabytes.movieviewer.R

class RowLabelWrapper(val rowName: String, val isLeft: Boolean) : SeatWrapperInterface {
    override val viewType: Int = R.layout.i_row_label
}