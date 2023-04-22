package com.example.tmdb.data.repository

import com.example.tmdb.MOVIE_DETAILS_RESPONSE
import com.example.tmdb.MovieDetailsResult
import com.example.tmdb.data.repository.remote.datasource.RemoteDataSource
import com.example.tmdb.data.repository.remote.model.MovieDetails
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.InputStream

class RepositoryTest {

    private val dispatcher = StandardTestDispatcher()
    private val remoteDataSource: RemoteDataSource = mock()
    private val repository: Repository = RepositoryImpl(remoteDataSource, mock(), dispatcher)

    @Test
    fun getMovieDetailsCallsRemoteDataSourcefetchMovieDetails() = runTest(dispatcher) {
        repository.getMovieDetails(movieId = 1)
        verify(remoteDataSource).fetchMovieDetails(movieId = 1)
    }

    @Test
    fun getMovieDetailsReturnsMovieDetailsIfResponseWasSuccessful() = runTest(dispatcher) {
        whenever(remoteDataSource.fetchMovieDetails(movieId = 1))
            .thenReturn(MovieDetailsResult
                .Success(getSuccessResponse(MOVIE_DETAILS_RESPONSE, MovieDetails::class.java)!!))
        val result = repository.getMovieDetails(movieId = 1)
        assertEquals("/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
            (result as MovieDetailsResult.Success).movieDetails.posterPath)
    }

    @Test
    fun getMovieDetailsReturnsErrorIfResponseWasError() = runTest(dispatcher) {
        val errorCode = 404
        whenever(remoteDataSource.fetchMovieDetails(movieId = 1))
            .thenReturn(MovieDetailsResult.Error(errorCode, null))
        val response = repository.getMovieDetails(movieId = 1)
        assertEquals(errorCode, (response as MovieDetailsResult.Error).code)
    }

    @Test
    fun getMovieDetailsReturnFailureIfResponseWasFailure() = runTest(dispatcher) {
        val failureMessage = "Wrong State"
        whenever(remoteDataSource.fetchMovieDetails(movieId = 1))
            .thenReturn(MovieDetailsResult.Failure(failureMessage))
        val response = repository.getMovieDetails(movieId = 1)
        assertEquals(failureMessage, (response as MovieDetailsResult.Failure).message)
    }

    private fun<T> getSuccessResponse(fileName: String, destination: Class<T>): T? {
        return try {
            val inputStream: InputStream = javaClass.classLoader!!
                .getResourceAsStream(fileName)
            val json = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(json, destination)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}