package com.wengabytes.movieviewer.components.seatmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.wengabytes.movieviewer.R
import com.wengabytes.movieviewer.base.BaseFragment
import com.wengabytes.movieviewer.base.SimpleMap
import com.wengabytes.movieviewer.components.seatmap.adapter.SeatMapListener
import com.wengabytes.movieviewer.components.seatmap.adapter.SeatmapAdapter
import com.wengabytes.movieviewer.utils.decorator.SeatMapDecoration
import kotlinx.android.synthetic.main.f_seatmap.*
import kotlinx.android.synthetic.main.i_selected_seat.view.*


class SeatmapFragment : BaseFragment<SeatmapFragmentVM>(), AdapterView.OnItemSelectedListener,
    SeatMapListener {

    private lateinit var seatmapAdapter: SeatmapAdapter

    private var currentZoomLevel = 1.0f

    // START: Implement Required Methods

    override fun provideVM(): SeatmapFragmentVM =
        ViewModelProviders.of(this, factory)[SeatmapFragmentVM::class.java]

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent ?: return

        when (parent.id) {
            spinner_date.id -> {
                vm.onDateChanged(position)
            }

            spinner_cinema.id -> {
                vm.onCinemaChange(position)
            }

            spinner_times.id -> {
                vm.onTimeChange(position)
            }
        }
    }

    override fun onSeatSelected(id: String, isSelected: Boolean) {
        vm.onSeatSelected(id, isSelected)
    }

    override fun onListBound() {
        baseInterface?.onLoading(false)
        textview_movie_screen_label.isVisible = true
    }

    // END  : Implement Required Methods

    // START: Callbacks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.f_seatmap, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seatmapAdapter = SeatmapAdapter(this)

        with(recycler_view_seats)
        {
            adapter = seatmapAdapter
            addItemDecoration(SeatMapDecoration(context))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listenVM()
    }

    // END  : Callbacks

    private lateinit var dateAdapter: ArrayAdapter<SimpleMap>
    private lateinit var cinemaAdapter: ArrayAdapter<SimpleMap>
    private lateinit var timesAdapter: ArrayAdapter<SimpleMap>

    // START: Custom Methods

    private fun listenVM() {
        vm.ldLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            baseInterface?.onLoading(isLoading)
        })

        vm.ldError.observe(viewLifecycleOwner, Observer {
            it?.consume()?.let { errorMessage ->
                baseInterface?.onError(errorMessage)
            }
        })

        vm.ldResetSelectionWithId.observe(viewLifecycleOwner, Observer {
            seatmapAdapter.resetSelectionOnId(it)
        })

        vm.ldUIModel.observe(viewLifecycleOwner, Observer {
            removeListeners()

            textview_theater.text = it.theaterName

            if (it.shouldRefreshDates) {
                dateAdapter = ArrayAdapter(
                    context!!,
                    R.layout.i_spinner,
                    it.listDates
                )
                dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_date.adapter = dateAdapter
            }

            if (it.shouldRefreshCinemas) {
                cinemaAdapter = ArrayAdapter(
                    context!!,
                    R.layout.i_spinner,
                    it.listCinemas
                )
                cinemaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_cinema.adapter = cinemaAdapter
            }


            if (it.shouldRefreshTimes) {
                timesAdapter = ArrayAdapter(
                    context!!,
                    R.layout.i_spinner,
                    it.listTimes
                )
                timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_times.adapter = timesAdapter
            }

            if (it.shouldUpdateSeatmap) {
                recycler_view_seats.layoutManager = GridLayoutManager(context, it.rowCount)
                recycler_view_seats.setHasFixedSize(true)
                seatmapAdapter.updateList(it.listSeatMap)
            }

            textview_selected_seat_label.isVisible = it.isListSelectedSeatsVisible

            addSelectedSeats(it.listSelectedSeats)

            textview_total.text = it.total

            addListeners()
        })
    }

    private fun removeListeners() {
        spinner_date.onItemSelectedListener = null
        spinner_cinema.onItemSelectedListener = null
        spinner_times.onItemSelectedListener = null
    }

    private fun addListeners() {
        spinner_date.onItemSelectedListener = this
        spinner_cinema.onItemSelectedListener = this
        spinner_times.onItemSelectedListener = this
    }

    private fun addSelectedSeats(list: List<String>) {
        grid_selected_seats_container.removeAllViews()

        list.forEach {
            val viewToAdd = layoutInflater.inflate(
                R.layout.i_selected_seat,
                grid_selected_seats_container,
                false
            )

            viewToAdd.textview_selected_seat.text = it

            grid_selected_seats_container.addView(viewToAdd)
        }
    }

    // END  : Custom Methods
}