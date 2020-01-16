package com.wengabytes.movieviewer.services

import com.wengabytes.movieviewer.services.movie.GetMovieRespModel
import com.wengabytes.movieviewer.services.schedule.GetScheduleRespModel
import com.wengabytes.movieviewer.services.seatmap.GetSeatmapRespModel
import retrofit2.http.GET

interface MovieAPI {
    @GET("movie.json")
    suspend fun getMovie(): GetMovieRespModel

    @GET("schedule.json")
    suspend fun getSchedule(): GetScheduleRespModel

    @GET("seatmap.json")
    suspend fun getSeatmap(): GetSeatmapRespModel
}