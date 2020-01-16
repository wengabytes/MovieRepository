package com.wengabytes.movieviewer.components.seatmap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.wengabytes.movieviewer.R
import kotlinx.android.synthetic.main.i_row_label.view.*
import kotlinx.android.synthetic.main.i_seat.view.*

class SeatmapAdapter(private val listener: SeatMapListener) :
    RecyclerView.Adapter<BaseVH>() {
    private val listSeatWrapperInterface = ArrayList<SeatWrapperInterface>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.i_row_label -> {
                RowLabelVH(inflater.inflate(viewType, parent, false))
            }
            R.layout.i_seat -> {
                SeatVH(inflater.inflate(viewType, parent, false))
            }
            R.layout.i_spacer -> {
                Spacer(inflater.inflate(viewType, parent, false))
            }
            else -> throw RuntimeException("Unexpected viewType")
        }
    }

    override fun getItemCount() = listSeatWrapperInterface.size

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        if (position == listSeatWrapperInterface.lastIndex)
            listener.onListBound()

        return holder.bind(listSeatWrapperInterface[position])
    }

    override fun getItemViewType(position: Int): Int =
        listSeatWrapperInterface[position].viewType

    fun updateList(list: List<SeatWrapperInterface>) {
        this.listSeatWrapperInterface.clear()
        this.listSeatWrapperInterface.addAll(list)
        notifyDataSetChanged()
    }

    // TODO: Refactor this to use payload. Used this approach because of time constraint
    fun resetSelectionOnId(id: String) {
        val wrapper = listSeatWrapperInterface.filterIsInstance<SeatWrapper>()
            .find { seatWrapper ->
                seatWrapper.id == id
            }

        val index = listSeatWrapperInterface.indexOf(wrapper as SeatWrapperInterface)

        wrapper.isSelected = !wrapper.isSelected
        notifyItemChanged(index)
    }


    inner class RowLabelVH(itemView: View) : BaseVH(itemView) {
        lateinit var wrapper: RowLabelWrapper

        override fun bind(wrapper: SeatWrapperInterface) {
            wrapper as RowLabelWrapper
            this.wrapper = wrapper

            itemView.textview_row_label.text = wrapper.rowName
        }
    }

    inner class SeatVH(itemView: View) : BaseVH(itemView), View.OnClickListener {

        override fun bind(wrapper: SeatWrapperInterface) {
            wrapper as SeatWrapper

            val drawableRes = if (wrapper.isAvailable) {
                itemView.setOnClickListener(this)
                R.color.colorSeatAvailable
            } else {
                itemView.setOnClickListener(null)
                R.color.colorSeatReserved
            }

            setImageDrawable(drawableRes)
        }

        override fun onClick(view: View?) {
            val wrapper = listSeatWrapperInterface[adapterPosition] as SeatWrapper

            wrapper.isSelected = !wrapper.isSelected
            listener.onSeatSelected(wrapper.id, wrapper.isSelected)

            toggleSelection(wrapper.isSelected)
        }

        private fun toggleSelection(isSelected: Boolean) {
            val drawableRes = if (isSelected)
                R.drawable.logo_seat_selected
            else
                R.color.colorSeatAvailable

            setImageDrawable(drawableRes)
        }

        private fun setImageDrawable(drawableRes: Int) {
            val drawable = AppCompatResources.getDrawable(itemView.context, drawableRes)
            itemView.imageview_seat.setImageDrawable(drawable)
        }
    }

    inner class Spacer(itemView: View) : BaseVH(itemView) {
        override fun bind(wrapper: SeatWrapperInterface) {
            wrapper as SpacerWrapper
        }
    }


}

abstract class BaseVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(wrapper: SeatWrapperInterface)
}

interface SeatMapListener {
    fun onListBound()
    fun onSeatSelected(id: String, isSelected: Boolean)
}