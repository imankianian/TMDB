package com.example.tmdb.data.repository.remote

import androidx.paging.PagingSource
import com.example.tmdb.MoviesResult
import com.example.tmdb.data.repository.TMDBPagingSource
import com.example.tmdb.data.repository.remote.datasource.RemoteDataSource
import com.example.tmdb.data.repository.remote.model.Movie
import com.example.tmdb.data.repository.remote.model.MovieResponse
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.InputStream

class TMDBPagingSourceTest {

    private val remoteDataSource: RemoteDataSource = mock()
    private val dispatcher = UnconfinedTestDispatcher()


    @Test
    fun getMoviesReturnsFlowOfPagingDataOfMoviesIfResponseWasSuccessful() = runTest(dispatcher) {
        whenever(remoteDataSource.fetchPopularMovies(page = 1))
            .thenReturn(MoviesResult.Success(getSuccessMovieResponse()!!))
        val tMDBPagingSource = TMDBPagingSource(remoteDataSource, dispatcher)
        assertEquals(
            PagingSource.LoadResult.Page(
                data = getSuccessMovieResponse()!!.movies,
                prevKey = null,
                nextKey = 2
            ),
            tMDBPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun getMoviesReturnsFlowOfPagingDataOfMoviesIfResponseReturnedError() = runTest(dispatcher) {
        val errorCode = 404
        val errorMessage = "No such content"
        whenever(remoteDataSource.fetchPopularMovies(page = 1))
            .thenReturn(MoviesResult.Error(errorCode, errorMessage))
        val tMDBPagingSource = TMDBPagingSource(remoteDataSource, dispatcher)
        assertEquals(
            PagingSource.LoadResult.Error<Int, Movie>(Throwable("$errorCode: $errorMessage"))
                .throwable.message,
            (tMDBPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            ) as PagingSource.LoadResult.Error).throwable.message
        )
    }

    @Test
    fun getMoviesReturnsFlowOfPagingDataOfMoviesIfResponseReturnedFailure() = runTest(dispatcher) {
        val failureMessage = "Failed to contact server"
        whenever(remoteDataSource.fetchPopularMovies(page = 1))
            .thenReturn(MoviesResult.Failure(failureMessage))
        val tMDBPagingSource = TMDBPagingSource(remoteDataSource, dispatcher)
        assertEquals(
            PagingSource.LoadResult.Error<Int, Movie>(Throwable("$failureMessage"))
                .throwable.message,
            (tMDBPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            ) as PagingSource.LoadResult.Error).throwable.message
        )
    }

    private fun getSuccessMovieResponse(): MovieResponse? {
        return try {
            val inputStream: InputStream = javaClass.classLoader!!
                .getResourceAsStream("PopularMoviesResponse.txt")
            val json = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(json, MovieResponse::class.java)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}