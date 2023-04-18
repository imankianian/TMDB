package com.example.tmdb.data.repository.remote.api

import com.example.tmdb.BuildConfig
import com.example.tmdb.data.repository.remote.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {

    @GET("3/movie/popular")
    suspend fun fetchPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int
    ): Response<MovieResponse>
}