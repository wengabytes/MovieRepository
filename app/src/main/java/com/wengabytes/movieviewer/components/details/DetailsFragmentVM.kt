package com.wengabytes.movieviewer.components.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wengabytes.movieviewer.base.BaseVM
import com.wengabytes.movieviewer.services.MovieRepository
import com.wengabytes.movieviewer.utils.Event
import com.wengabytes.movieviewer.utils.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragmentVM(
    context: Context,
    private val repo: MovieRepository
) : BaseVM(context) {

    private val _ldUIModel = MutableLiveData<UIModel>()
    val ldUIModel: LiveData<UIModel>
        get() = _ldUIModel

    val ldNavigateToSeatmap = MutableLiveData<Event<Boolean>>()


    init {
        getMovieDetails()
    }

    // START: WS Calls
    fun getMovieDetails() {
        if (job?.isActive == true)
            job?.cancel()

        job = scope.launch {
            withContext(Dispatchers.Main) {
                ldLoading.value = true
            }

            val result = withContext(Dispatchers.IO) {
                repo.getMovieDetails(context)
            }

            withContext(Dispatchers.Main) {
                when (result) {
                    is NetworkResult.Success -> {
                        val resp = result.data

                        _ldUIModel.value = UIModel(
                            name = resp.canonicalTitle,
                            genre = resp.genre,
                            advisoryRating = resp.advisoryRating,
                            duration = formatDuration(resp.runtimeMins),
                            releaseDate = formatReleaseDate(resp.releaseDate),
                            synopsis = resp.synopsis,
                            casts = formatCasts(resp.listCast),

                            linkPoster = resp.posterLink,
                            linkPosterLandscape = resp.posterLandscapeLink
                        )
                    }

                    is NetworkResult.Error -> ldError.value = Event(result.errorMessage)
                }

                ldLoading.value = false
            }
        }
    }

    fun getMovieSchedule() {
        if (job?.isActive == true)
            return

        job = scope.launch {
            withContext(Dispatchers.Main) {
                ldLoading.value = true
            }

            val result = withContext(Dispatchers.IO) {
                repo.getMovieSchedule(context)
            }

            withContext(Dispatchers.Main) {
                when (result) {
                    is NetworkResult.Success -> {
                        ldNavigateToSeatmap.value = Event(true)
                    }

                    is NetworkResult.Error -> ldError.value = Event(result.errorMessage)
                }

                ldLoading.value = false
            }
        }
    }
    // END  : WS Calls

    private fun formatDuration(runtimeInMinutes: String): String {
        val sb = StringBuilder()

        val minutes = try {
            runtimeInMinutes.toFloat()
                .toInt() // API returns float. Only hrs and mins are needed, drop the decimal places
        } catch (e: Exception) {
            0
        }

        if (minutes == 0)
            return ""

        val noOfHrs = minutes / 60
        val noOfMins = minutes % 60

        if (noOfHrs > 0) {
            sb.append(noOfHrs)
            if (noOfHrs > 1)
                sb.append("hrs ")
            else
                sb.append("hr ")
        }

        if (noOfMins > 0) {
            sb.append(noOfMins)
            if (noOfMins > 1)
                sb.append("mins")
            else
                sb.append("min")
        }

        return sb.toString()
    }

    private fun formatReleaseDate(unformattedReleaseDate: String): String {
        return if (unformattedReleaseDate.isEmpty())
            ""
        else {
            val date = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(unformattedReleaseDate)

            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        }
    }

    private fun formatCasts(listCasts: List<String>): String
    {
        val sb = StringBuilder()

        listCasts.forEach {
            sb.append(it)
            sb.append(",\n")
        }

        return sb.removeSuffix(",\n").toString()
    }
}

data class UIModel(
    val name: String = "",
    val genre: String = "",
    val advisoryRating: String = "",
    val duration: String = "",
    val releaseDate: String = "",
    val synopsis: String = "",
    val casts: String = "",

    val linkPosterLandscape: String = "",
    val linkPoster: String = ""
)