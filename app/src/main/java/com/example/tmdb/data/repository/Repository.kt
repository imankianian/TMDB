package com.example.tmdb.data.repository

import androidx.paging.PagingData
import com.example.tmdb.MovieDetailsResult
import com.example.tmdb.data.repository.remote.model.Movie
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getMovies(): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResult
}