package com.example.tmdb.data.repository.remote.api

import com.example.tmdb.BuildConfig
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream

class TMDBApiTest {

    private val POPULAR_MOVIES_RESPONSE = "PopularMoviesResponse.txt"
    private val MOVIE_DETAILS_RESPONSE = "MovieDetailsResponse.txt"

    private val mockWebServer by lazy {
        MockWebServer()
    }

    private val tMDBApi by lazy {
        Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(TMDBApi::class.java)
    }

    @Test fun fetchPopularMoviesCallsCorrectEndpoint() = runTest {
        mockWebServer.enqueue(MockResponse()
            .setBody(getResponseFromFile(POPULAR_MOVIES_RESPONSE)!!).setResponseCode(200))
        tMDBApi.fetchPopularMovies(page = 1)
        assertEquals("/3/movie/popular?api_key=${BuildConfig.API_KEY}&page=1",
            mockWebServer.takeRequest().path)
    }

    @Test
    fun fetchMoviesReturnsMovies() = runTest {
        mockWebServer.enqueue(MockResponse()
            .setBody(getResponseFromFile(POPULAR_MOVIES_RESPONSE)!!).setResponseCode(200))
        val movies = tMDBApi.fetchPopularMovies(page = 1)
        assertEquals(20, movies.body()!!.movies.size)
        assertEquals("The Super Mario Bros. Movie", movies.body()!!.movies[0].title)
    }

    @Test
    fun fetchMovieDetailsCallsCorrectEndpoint() = runTest {
        var movieId = 1
        mockWebServer.enqueue(MockResponse()
            .setBody(getResponseFromFile(MOVIE_DETAILS_RESPONSE)!!).setResponseCode(200))
        tMDBApi.fetchMovieDetails(movieId = 1)
        assertEquals("/3/movie/$movieId?api_key=${BuildConfig.API_KEY}",
            mockWebServer.takeRequest().path)
    }

    @Test
    fun fetchMovieDetailsReturnMovieDetails() = runTest {
        mockWebServer.enqueue(MockResponse()
            .setBody(getResponseFromFile(MOVIE_DETAILS_RESPONSE)!!).setResponseCode(200))
        val movieDetails = tMDBApi.fetchMovieDetails(movieId = 1)
        assertEquals("/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg", movieDetails.body()!!.posterPath)
    }

    private fun getResponseFromFile(fileName: String): String? {
        return try {
            val  inputStream:InputStream = javaClass.classLoader!!
                .getResourceAsStream(fileName)
            inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}