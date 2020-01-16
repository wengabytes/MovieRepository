package com.wengabytes.movieviewer.utils.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wengabytes.movieviewer.R
import com.wengabytes.movieviewer.components.seatmap.adapter.SeatmapAdapter

class SeatMapDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val viewHolder = parent.getChildViewHolder(view)
        val viewType = viewHolder.itemViewType
        val space = context.resources.getDimensionPixelOffset(R.dimen.spacing_default_half) / 2

        when (viewType) {
            R.layout.i_row_label -> {
                if ((viewHolder as SeatmapAdapter.RowLabelVH).wrapper.isLeft) {
                    outRect.set(0, 0, space, 0)
                } else {
                    outRect.set(space, 0, 0, 0)
                }
            }

            R.layout.i_spacer -> outRect.set(0, 0, 0, 0)
            R.layout.i_seat -> outRect.set(space / 2, 0, space / 2, 0)
        }
    }

}