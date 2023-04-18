package com.example.tmdb

import com.example.tmdb.data.repository.remote.model.MovieResponse

const val BASE_URL = "https://api.themoviedb.org"
const val TAG = "TMDB ==>"

sealed interface MoviesResult {
    data class Success(val movieResponse: MovieResponse): MoviesResult
    data class Error(val code: Int?, val message: String?): MoviesResult
    data class Failure(val message: String?): MoviesResult
}