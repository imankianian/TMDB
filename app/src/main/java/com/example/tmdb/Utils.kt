package com.example.tmdb

import android.util.Log
import com.example.tmdb.data.repository.remote.model.MovieDetails
import com.example.tmdb.data.repository.remote.model.MovieResponse
import kotlin.text.Typography.bullet

const val BASE_URL = "https://api.themoviedb.org"
const val TAG = "TMDB ==>"
const val STARTING_PAGE_INDEX = 1
const val PAGE_SIZE = 10
const val MOVIES_IMAGE_PATH = "https://image.tmdb.org/t/p/w500"
const val MOVIE_DETAILS_IMAGE_PATH = "https://image.tmdb.org/t/p/original"

sealed interface MoviesResult {
    data class Success(val movieResponse: MovieResponse): MoviesResult
    data class Error(val code: Int?, val message: String?): MoviesResult
    data class Failure(val message: String?): MoviesResult
}

data class Genre(
    val id: Int,
    val name: String
)

sealed interface MovieDetailsResult {
    data class Success(val movieDetails: MovieDetails): MovieDetailsResult
    data class Error(val code: Int?, val message: String?): MovieDetailsResult
    data class Failure(val message: String?): MovieDetailsResult
}

sealed interface UiState {
    object Loading: UiState
    data class Success(val movieDetails: MovieDetails): UiState
    data class Error(val message: String?): UiState
}

fun getGenresString(genres: List<Genre>): String {
    var result = StringBuilder()
    result.append("$bullet ")
    genres.forEachIndexed { index, genre ->
        if (index > 0) {
            result.append(", ")
        }
        result.append(" ${genre.name}")
    }
    result.append(" $bullet")
    Log.d(TAG, result.toString())
    return result.toString()
}