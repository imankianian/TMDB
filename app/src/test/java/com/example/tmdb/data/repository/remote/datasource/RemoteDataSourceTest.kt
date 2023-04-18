package com.example.tmdb.data.repository.remote.datasource

import com.example.tmdb.MoviesResult
import com.example.tmdb.data.repository.remote.api.TMDBApi
import com.example.tmdb.data.repository.remote.model.MovieResponse
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.InputStream

class RemoteDataSourceTest {

    private val tMDBApi: TMDBApi = mock()
    private val dispatcher = StandardTestDispatcher()
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl(tMDBApi, dispatcher)

    @Test
    fun fetchPopularMoviesCallsTMDBApifetchPopularMovies() = runTest(dispatcher) {
        remoteDataSource.fetchPopularMovies(1)
        verify(tMDBApi).fetchPopularMovies(page = 1)
    }

    @Test
    fun fetchPopularMoviesReceivesMoviesIfResponseWasSuccessful() = runTest(dispatcher) {
        whenever(tMDBApi.fetchPopularMovies(page = 1)).thenReturn(Response.success(getSuccessMovieResponse()))
        val response = remoteDataSource.fetchPopularMovies(page = 1)
        assertEquals("The Super Mario Bros. Movie",
            (response as MoviesResult.Success).movieResponse.movies[0].title)
    }

    @Test
    fun fetchPopularMoviesReceivesErrorIfResponseWasError() = runTest(dispatcher) {
        val errorCode = 404
        whenever(tMDBApi.fetchPopularMovies(page = 1)).thenReturn(Response.error(errorCode,
            "".toResponseBody()))
        val response = remoteDataSource.fetchPopularMovies(page = 1)
        assertEquals(errorCode, (response as MoviesResult.Error).code)
    }

    @Test
    fun fetchPopularMoviesReceivesFailureIfResponseWasFailure() = runTest(dispatcher) {
        val failureMessage = "Wrong State"
        whenever(tMDBApi.fetchPopularMovies(page = 1)).thenThrow(IllegalStateException(failureMessage))
        val response = remoteDataSource.fetchPopularMovies(page = 1)
        assertEquals(failureMessage, (response as MoviesResult.Failure).message)
    }

    private fun getSuccessMovieResponse(): MovieResponse? {
        return try {
            val inputStream: InputStream = javaClass.classLoader!!
                .getResourceAsStream("SampleResponse.txt")
            val json = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(json, MovieResponse::class.java)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}