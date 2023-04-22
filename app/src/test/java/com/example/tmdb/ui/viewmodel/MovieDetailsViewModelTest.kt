package com.example.tmdb.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.tmdb.MOVIE_DETAILS_RESPONSE
import com.example.tmdb.MovieDetailsResult
import com.example.tmdb.UiState
import com.example.tmdb.data.repository.Repository
import com.example.tmdb.data.repository.RepositoryImpl
import com.example.tmdb.data.repository.remote.model.MovieDetails
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.InputStream

class MovieDetailsViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val repository: Repository = RepositoryImpl(mock(), mock(), dispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun getMovieDetailsReturnsLoadingUiStateAfterInitialization() = runTest(dispatcher.scheduler) {
        val viewModel = MovieDetailsViewModel(repository, SavedStateHandle())
        assertEquals(UiState.Loading, (viewModel.uiState.value as UiState.Loading))
    }

    @Test
    fun getMovieDetailsReturnsSuccessUiStateIfResponseWasSuccessful() = runTest(dispatcher.scheduler) {
        whenever(repository.getMovieDetails(movieId = 1))
            .thenReturn(MovieDetailsResult
                .Success(getSuccessResponse(MOVIE_DETAILS_RESPONSE, MovieDetails::class.java)!!))
        val viewModel = MovieDetailsViewModel(repository, SavedStateHandle(mapOf(Pair("movie_id", "1"))))
        assertEquals("/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
            (viewModel.uiState.value as UiState.Success).movieDetails.posterPath)
    }

    @Test
    fun getMovieDetailsReturnsErrorUiStateIfResponseReturnedError() = runTest(dispatcher.scheduler) {
        val errorCode = 404
        val errorMessage = "Resource Not Found"
        whenever(repository.getMovieDetails(movieId = 1))
            .thenReturn(MovieDetailsResult.Error(404, errorMessage))
        val viewModel = MovieDetailsViewModel(repository, SavedStateHandle(mapOf(Pair("movie_id", "1"))))
        assertEquals("$errorCode: $errorMessage", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test
    fun getMovieDetailsReturnsErrorUiStateIfResponseFailed() = runTest(dispatcher.scheduler) {
        val failureMessage = "Failed to contact server"
        whenever(repository.getMovieDetails(movieId = 1))
            .thenReturn(MovieDetailsResult.Failure(failureMessage))
        val viewModel = MovieDetailsViewModel(repository, SavedStateHandle(mapOf(Pair("movie_id", "1"))))
        assertEquals(failureMessage, (viewModel.uiState.value as UiState.Error).message)
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