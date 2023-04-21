package com.example.tmdb.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.tmdb.IMAGE_PATH
import com.example.tmdb.R
import com.example.tmdb.data.repository.remote.model.Movie

@Composable
fun MoviesScreen(movies: LazyPagingItems<Movie>) {
    DisplayMovies(movies)
}

@Composable
fun DisplayMovies(movies: LazyPagingItems<Movie>) {
    Column {
        when (val state = movies.loadState.refresh) {
            is LoadState.Loading -> { // Loading UI
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Refresh Loading"
                    )
                    LoadingScreen()
                }
            }
            is LoadState.Error -> {
                Column {
                    ErrorScreen(message = state.error.message ?: "Error Loading Data")
                }
            }
            else -> {}
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 185.dp),
            modifier = Modifier.padding(5.dp)) {
            items(count = movies.itemCount) { index ->
                movies[index]?.let { movie ->
                    DisplayMovie(movie)
                }
            }
            when (val state = movies.loadState.append) {
                is LoadState.Loading -> { // Loading UI
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = "Refresh Loading"
                            )
                            LoadingScreen()
                        }
                    }
                }
                is LoadState.Error -> {
                    item {
                        Column {
                            ErrorScreen(message = state.error.message ?: "Error Loading Data")
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun DisplayMovie(movie: Movie) {
    Column(modifier = Modifier
        .width(185.dp)
        .padding(5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color(0xff1a1a1a))) {
        movie.posterPath?.let { poster ->
            AsyncImage(model = IMAGE_PATH + poster,
                contentDescription = "movie poster",
                modifier = Modifier.width(190.dp))
        }
        Column(modifier = Modifier.padding(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(model = R.drawable.ic_star,
                    contentDescription = "star count",
                    modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = movie.voteAverage.toString(),
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = movie.title,
                fontSize = 13.sp ,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = movie.releaseDate.substring(0..3),
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
    }
}