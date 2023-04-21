package com.example.tmdb.data.repository.remote.datasource

import android.util.Log
import com.example.tmdb.MovieDetailsResult
import com.example.tmdb.MoviesResult
import com.example.tmdb.TAG
import com.example.tmdb.data.repository.remote.api.TMDBApi
import com.example.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val tMDBApi: TMDBApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
    ): RemoteDataSource {

    override suspend fun fetchPopularMovies(page: Int) = withContext(dispatcher) {
        try {
            val response = tMDBApi.fetchPopularMovies(page = 1)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "API response -> Success")
                MoviesResult.Success(response.body()!!)
            } else {
                Log.d(TAG, "API response -> Error")
                MoviesResult.Error(response.code(), response.message())
            }
        } catch (exception: Exception) {
            Log.d(TAG, "API response -> Failure")
            MoviesResult.Failure(exception.message)
        }
    }

    override suspend fun fetchMovieDetails(movieId: Int) = withContext(dispatcher) {
        try {
            val response = tMDBApi.fetchMovieDetails(movieId)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "API response -> Success")
                MovieDetailsResult.Success(response.body()!!)
            } else {
                Log.d(TAG, "API response -> Error")
                MovieDetailsResult.Error(response.code(), response.message())
            }
        } catch (exception: Exception) {
            MovieDetailsResult.Failure(exception.message)
        }
    }
}