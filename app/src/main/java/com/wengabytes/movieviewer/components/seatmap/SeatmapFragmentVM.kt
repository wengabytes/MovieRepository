package com.wengabytes.movieviewer.components.seatmap

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wengabytes.movieviewer.R
import com.wengabytes.movieviewer.base.BaseVM
import com.wengabytes.movieviewer.base.SimpleMap
import com.wengabytes.movieviewer.components.seatmap.adapter.RowLabelWrapper
import com.wengabytes.movieviewer.components.seatmap.adapter.SeatWrapper
import com.wengabytes.movieviewer.components.seatmap.adapter.SeatWrapperInterface
import com.wengabytes.movieviewer.components.seatmap.adapter.SpacerWrapper
import com.wengabytes.movieviewer.services.MovieRepository
import com.wengabytes.movieviewer.utils.Event
import com.wengabytes.movieviewer.utils.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class SeatmapFragmentVM(
    context: Context,
    private val repo: MovieRepository
) : BaseVM(context) {

    private val _ldUIModel = MutableLiveData<UIModel>()
    val ldUIModel: LiveData<UIModel>
        get() = _ldUIModel

    private val _ldResetSelectionWithId = MutableLiveData<String>()
    val ldResetSelectionWithId: LiveData<String>
        get() = _ldResetSelectionWithId

    private val listSelectedSeats = ArrayList<String>()

    private var price: Int = 0
    private var prevSelectedDatePos = 0
    private var prevSelectedCinemaPos = 0
    private var prevSelectedTimePos = 0

    init {
        val theaterName = repo.movieDetailsResp?.theater ?: ""
        repo.scheduleRespModel?.let {
            val listSimpleMapDates = getListSimpleMapDates()
            val listSimpleMapCinemas = getListSimpleMapCinemas(listSimpleMapDates.first().id)
            val listSimpleMapTimes = getListSimpleMapTimes(listSimpleMapCinemas.first().id)


            val uiModel = UIModel(
                theaterName = theaterName,
                listDates = listSimpleMapDates,
                listCinemas = listSimpleMapCinemas,
                listTimes = listSimpleMapTimes,

                shouldRefreshDates = true,
                shouldRefreshCinemas = true,
                shouldRefreshTimes = true,
                shouldUpdateSeatmap = false
            )

            setPrice(listSimpleMapCinemas.first().id, listSimpleMapTimes.first().id)

            _ldUIModel.value = uiModel
        }

        getSeatmap()

    }

    private fun getListSimpleMapDates(): List<SimpleMap> {
        val list = ArrayList<SimpleMap>()

        repo.scheduleRespModel?.listDate
            ?.forEach {
                list.add(SimpleMap(it.id, it.label))
            }

        return list
    }

    private fun getListSimpleMapCinemas(dateId: String): List<SimpleMap> {
        val list = ArrayList<SimpleMap>()

        repo.scheduleRespModel?.listCinemaHolderBeans
            ?.find { it.parent == dateId }
            ?.listCinemaBeans?.forEach {
            list.add(SimpleMap(it.id, it.label))
        }

        return list
    }

    private fun getListSimpleMapTimes(cinemaId: String): List<SimpleMap> {
        val list = ArrayList<SimpleMap>()

        repo.scheduleRespModel?.listTimeHolderBean
            ?.find { it.parent == cinemaId }
            ?.listTimeBeans?.forEach {
            list.add(SimpleMap(it.id, it.label))
        }

        return list
    }

    private fun setPrice(cinemaId: String, timeId: String) {
        price = Integer.parseInt(repo.scheduleRespModel?.listTimeHolderBean
            ?.find { it.parent == cinemaId }
            ?.listTimeBeans?.find { it.id == timeId }
            ?.price ?: "0")

        val total: Int = listSelectedSeats.size * price
        val formattedAmount = context.getString(R.string.format_amount, formatAmount(total))

        _ldUIModel.value = _ldUIModel.value?.copy(
            total = formattedAmount,
            shouldRefreshDates = false,
            shouldRefreshCinemas = false,
            shouldRefreshTimes = false,
            shouldUpdateSeatmap = false
        )
    }

    fun onDateChanged(position: Int) {
        if (position == prevSelectedDatePos)
            return

        val listDates = getListSimpleMapDates()
        val listCinemas = getListSimpleMapCinemas(listDates[position].id)
        val listTimes = getListSimpleMapTimes(listCinemas.firstOrNull()?.id ?: "")

        _ldUIModel.value = _ldUIModel.value?.copy(
            listCinemas = listCinemas,
            listTimes = listTimes,
            shouldRefreshDates = false,
            shouldRefreshCinemas = true,
            shouldRefreshTimes = true,
            shouldUpdateSeatmap = false
        )

        prevSelectedDatePos = position

        //getSeatmap()
    }

    fun onCinemaChange(position: Int) {
        if (position == prevSelectedCinemaPos)
            return

        val listDates = getListSimpleMapDates()
        val listCinemas = getListSimpleMapCinemas(listDates[prevSelectedDatePos].id)
        val listTimes = getListSimpleMapTimes(listCinemas[position].id)
        _ldUIModel.value = _ldUIModel.value?.copy(
            listTimes = listTimes,
            shouldRefreshDates = false,
            shouldRefreshCinemas = false,
            shouldRefreshTimes = true,
            shouldUpdateSeatmap = false
        )

        prevSelectedCinemaPos = position

        //getSeatmap()
    }

    fun onTimeChange(position: Int) {
        if (position == prevSelectedTimePos)
            return

        val listDates = getListSimpleMapDates()
        val listCinemas = getListSimpleMapCinemas(listDates[prevSelectedDatePos].id)
        val listTimes = getListSimpleMapTimes(listCinemas[prevSelectedCinemaPos].id)

        setPrice(listCinemas[prevSelectedCinemaPos].id, listTimes[position].id)

        prevSelectedTimePos = position

        //getSeatmap()
    }

    fun onSeatSelected(id: String, isSelected: Boolean) {
        if (isSelected) {
            if (listSelectedSeats.size < 10)
                listSelectedSeats.add(id)
            else {
                _ldResetSelectionWithId.value = id
                ldError.value = Event(context.getString(R.string.errmsg_max_seats_selected))
            }
        } else {
            listSelectedSeats.remove(id)
        }

        val total: Int = listSelectedSeats.size * price
        val formattedAmount = context.getString(R.string.format_amount, formatAmount(total))

        _ldUIModel.value = _ldUIModel.value?.copy(
            listSelectedSeats = listSelectedSeats,
            isListSelectedSeatsVisible = listSelectedSeats.size > 0,
            total = formattedAmount,
            shouldRefreshDates = false,
            shouldRefreshCinemas = false,
            shouldRefreshTimes = false,
            shouldUpdateSeatmap = false
        )
    }

    private fun formatAmount(amount: Number): String {
        val decimalFormatter = DecimalFormat().apply {
            decimalFormatSymbols = DecimalFormatSymbols(Locale.ENGLISH).apply {
                groupingSeparator = ','
                decimalSeparator = '.'
            }
            isGroupingUsed = true
            minimumFractionDigits = 2
            maximumFractionDigits = 2
            groupingSize = 3
        }

        return decimalFormatter.format(amount) ?: "0.00"
    }

    private fun getSeatmap() {
        if (job?.isActive == true)
            return

        job = scope.launch {
            withContext(Dispatchers.Main) {
                ldLoading.value = true
            }

            val result = withContext(Dispatchers.IO) {
                repo.getSeatmap(context)
            }

            withContext(Dispatchers.Main) {
                when (result) {
                    is NetworkResult.Success -> formatSeatmap()
                    is NetworkResult.Error -> ldError.value = Event(result.errorMessage)
                }
            }
        }

    }

    private fun formatSeatmap() {
        var rowCount = 0

        val listSeatMap = ArrayList<SeatWrapperInterface>().apply {
            repo.seatmapRespModel?.listSeatmaps?.forEach { row ->
                add(RowLabelWrapper(row.first().substring(0, 1), true))

                row.forEach { seat ->
                    if (seat.contains(Regex("\\(\\d{1,2}\\)"))) {
                        add(SpacerWrapper())
                    } else {
                        val isAvailable: Boolean = repo.seatmapRespModel
                            ?.availableSeats
                            ?.listSeats
                            ?.find { seat == it } != null

                        add(
                            SeatWrapper(
                                id = seat,
                                isAvailable = isAvailable
                            )
                        )
                    }
                }

                add(RowLabelWrapper(row.first().substring(0, 1), false))

                rowCount = row.size + 2
            }
        }

        listSelectedSeats.clear()

        _ldUIModel.value = _ldUIModel.value?.copy(
            total = "0.00",
            listSeatMap = listSeatMap,
            isListSelectedSeatsVisible = false,
            shouldRefreshDates = false,
            shouldRefreshCinemas = false,
            shouldRefreshTimes = false,
            shouldUpdateSeatmap = true,
            rowCount = rowCount
        )
    }
}

data class UIModel(
    val theaterName: String = "",
    val listDates: List<SimpleMap> = emptyList(),
    val listCinemas: List<SimpleMap> = emptyList(),
    val listTimes: List<SimpleMap> = emptyList(),

    val listSelectedSeats: List<String> = emptyList(),
    val total: String = "0.00",

    val shouldRefreshDates: Boolean = false,
    val shouldRefreshCinemas: Boolean = false,
    val shouldRefreshTimes: Boolean = false,
    val shouldUpdateSeatmap: Boolean = false,

    val rowCount: Int = 0,

    val isListSelectedSeatsVisible: Boolean = false,
    val listSeatMap: List<SeatWrapperInterface> = emptyList()
)