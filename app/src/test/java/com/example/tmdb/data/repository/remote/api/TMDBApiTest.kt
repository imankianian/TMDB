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

    private val mockWebServer by lazy {
        MockWebServer()
    }

    private val tMDBApi by lazy {
        Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(TMDBApi::class.java)
    }

    @Test fun fetchPopularMoviesCallsCorrectEndpoint() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(readSampleResponse()!!).setResponseCode(200))
        tMDBApi.fetchPopularMovies(page = 1)
        assertEquals("/3/movie/popular?api_key=${BuildConfig.API_KEY}&page=1",
            mockWebServer.takeRequest().path)
    }

    @Test
    fun fetchMoviesReturnsMovies() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(readSampleResponse()!!).setResponseCode(200))
        val movies = tMDBApi.fetchPopularMovies(page = 1)
        assertEquals(20, movies.body()!!.movies.size)
        assertEquals("The Super Mario Bros. Movie", movies.body()!!.movies[0].title)
    }

    private fun readSampleResponse(): String? {
        return try {
            val  inputStream:InputStream = javaClass.classLoader!!
                .getResourceAsStream("SampleResponse.txt")
            inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}