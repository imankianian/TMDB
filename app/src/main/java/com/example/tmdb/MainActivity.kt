package com.example.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.tmdb.ui.screen.MovieDetailsScreen
import com.example.tmdb.ui.screen.MoviesScreen
import com.example.tmdb.ui.theme.TMDBTheme
import com.example.tmdb.ui.viewmodel.MovieDetailsViewModel
import com.example.tmdb.ui.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TMDBTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.Movies.route) {
                        composable(Routes.Movies.route) {
                            val moviesViewModel = hiltViewModel<MoviesViewModel>()
                            val movies = moviesViewModel.getMovies().collectAsLazyPagingItems()
                            MoviesScreen(movies) { movieId ->
                                navController.navigate("Movie/$movieId")
                            }
                        }
                        composable(Routes.MovieDetails.route) {
                            val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
                            MovieDetailsScreen(movieDetailsViewModel)
                        }
                    }
                }
            }
        }
    }
}

sealed class Routes(val route: String) {
    object Movies: Routes("Movies")
    object MovieDetails: Routes("Movie/{movie_id}")
}