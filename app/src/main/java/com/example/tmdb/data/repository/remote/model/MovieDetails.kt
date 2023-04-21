package com.example.tmdb.data.repository.remote.model

import com.example.tmdb.Genre
import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    val genres: List<Genre>,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String,
    val runtime: Int?,
    @SerializedName("vote_average") val voteAverage: Float,
    val overview: String?
)
