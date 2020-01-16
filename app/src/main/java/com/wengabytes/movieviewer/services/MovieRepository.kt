package com.wengabytes.movieviewer.services

import android.content.Context
import com.wengabytes.movieviewer.services.movie.GetMovieRespModel
import com.wengabytes.movieviewer.services.schedule.GetScheduleRespModel
import com.wengabytes.movieviewer.services.seatmap.GetSeatmapRespModel
import com.wengabytes.movieviewer.utils.network.NetworkResult
import com.wengabytes.movieviewer.utils.network.callWebservice
import kotlinx.coroutines.delay
import org.koin.dsl.module

val movieRepository = module {
    single { MovieRepository(get()) }
}

class MovieRepository(private val api: MovieAPI) {
    var movieDetailsResp: GetMovieRespModel? = null
        private set

    var scheduleRespModel: GetScheduleRespModel? = null
        private set

    var seatmapRespModel: GetSeatmapRespModel? = null
        private set

    suspend fun getMovieDetails(context: Context): NetworkResult<GetMovieRespModel> {
        //delay(2000)

        val result = callWebservice(context) {
            api.getMovie()
        }

        if (result is NetworkResult.Success)
            movieDetailsResp = result.data

        return result
    }

    suspend fun getMovieSchedule(context: Context): NetworkResult<GetScheduleRespModel> {
        //delay(1000)

        val result = callWebservice(context) {
            api.getSchedule()
        }

        if (result is NetworkResult.Success) {
            scheduleRespModel = result.data
        }

        return result
    }

    suspend fun getSeatmap(context: Context): NetworkResult<GetSeatmapRespModel> {
        //delay(1000)

        val result = callWebservice(context) {
            api.getSeatmap()
        }

        if (result is NetworkResult.Success) {
            seatmapRespModel = result.data
        }

        return result
    }
}