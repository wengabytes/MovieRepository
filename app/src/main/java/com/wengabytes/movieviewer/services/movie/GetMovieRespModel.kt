package com.wengabytes.movieviewer.services.movie

import com.google.gson.annotations.SerializedName

class GetMovieRespModel(
    @SerializedName("movie_id")
    val movieId: String,

    @SerializedName("advisory_rating")
    val advisoryRating: String,

    @SerializedName("canonical_title")
    val canonicalTitle: String,

    @SerializedName("cast")
    val listCast: List<String>,

    @SerializedName("genre")
    val genre: String,

    @SerializedName("has_schedules")
    val hasSchedules: Int,

    @SerializedName("is_inactive")
    val isInactive: Int,

    @SerializedName("is_showing")
    val isShowing: Int,

    @SerializedName("link_name")
    val linkName: String,

    @SerializedName("poster")
    val posterLink: String,

    @SerializedName("poster_landscape")
    val posterLandscapeLink: String,

    @SerializedName("ratings")
    val listRatings: Any,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("runtime_mins")
    val runtimeMins: String,

    @SerializedName("synopsis")
    val synopsis: String,

    @SerializedName("trailer")
    val trailer: String,

    @SerializedName("average_rating")
    val averageRating: String?,

    @SerializedName("total_reviews")
    val totalREviews: String?,

    @SerializedName("variants")
    val listVariants: List<String>,

    @SerializedName("theater")
    val theater: String,

    @SerializedName("order")
    val order: Int,

    @SerializedName("is_featured")
    val isFeatured: Int,

    @SerializedName("watch_list")
    val isInWatchList: Boolean,

    @SerializedName("your_rating")
    val yourRating: Int
)