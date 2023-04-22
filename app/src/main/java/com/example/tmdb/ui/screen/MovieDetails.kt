package com.example.tmdb.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tmdb.*
import com.example.tmdb.R
import com.example.tmdb.data.repository.remote.model.MovieDetails
import com.example.tmdb.ui.viewmodel.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen(movieDetailsViewModel: MovieDetailsViewModel) {
    when (val result = movieDetailsViewModel.uiState.collectAsStateWithLifecycle().value) {
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Success -> {
            DisplayMovieDetails(result.movieDetails)
        }
        is UiState.Error -> {
            ErrorScreen(result.message ?: "Error in Loading")
        }
    }
}

@Composable
fun DisplayMovieDetails(movieDetails: MovieDetails) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        movieDetails.posterPath?.let { poster ->
            AsyncImage(model = MOVIE_DETAILS_IMAGE_PATH + poster,
                contentDescription = "movie poster",
                modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier.height(10.dp))
        val year = movieDetails.releaseDate.substring(0 until 4)
        Text(text = movieDetails.title + " ($year)",
            color = Color.LightGray,
            fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = R.drawable.ic_star,
                contentDescription = "star count",
                modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = String.format("%.1f", movieDetails.voteAverage),
                fontSize = 15.sp,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "${movieDetails.runtime.toString()} min",
                fontSize = 15.sp,
                color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = getGenresString(movieDetails.genres),
            fontSize = 10.sp,
            color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))
        Column(horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(10.dp)) {
            Text(text = "Overview",
                fontSize = 15.sp,
                color = Color.LightGray)
            movieDetails.overview?.let {
                Text(text = it,
                    fontSize = 13.sp,
                    color = Color.Gray)
            }
        }
    }
}