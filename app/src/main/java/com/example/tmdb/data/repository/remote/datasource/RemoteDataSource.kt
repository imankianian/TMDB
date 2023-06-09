package com.example.tmdb.data.repository.remote.datasource

import com.example.tmdb.MovieDetailsResult
import com.example.tmdb.MoviesResult

interface RemoteDataSource {

    suspend fun fetchPopularMovies(page: Int): MoviesResult
    suspend fun fetchMovieDetails(movieId: Int): MovieDetailsResult
}