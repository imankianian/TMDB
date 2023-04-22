package com.example.tmdb.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.MovieDetailsResult
import com.example.tmdb.UiState
import com.example.tmdb.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val movieId = savedStateHandle.get<String>("movie_id")
    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        movieId?.let { id ->
            loadMovieDetails(id.toInt())
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            when(val result = repository.getMovieDetails(movieId)) {
                is MovieDetailsResult.Success -> {
                    _uiState.value = UiState.Success(result.movieDetails)
                }
                is MovieDetailsResult.Error -> {
                    _uiState.value = UiState.Error("${result.code}: ${result.message}")
                }
                is MovieDetailsResult.Failure -> {
                    _uiState.value = UiState.Error("${result.message}")
                }
            }
        }
    }
}