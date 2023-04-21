package com.example.tmdb

import com.example.tmdb.data.repository.remote.model.MovieDetails
import com.example.tmdb.data.repository.remote.model.MovieResponse

const val BASE_URL = "https://api.themoviedb.org"
const val TAG = "TMDB ==>"
const val STARTING_PAGE_INDEX = 1
const val PAGE_SIZE = 10
const val IMAGE_PATH = "https://image.tmdb.org/t/p/w500"

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