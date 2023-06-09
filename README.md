# Sample TMDB app
This sample project displays a list of popular movies from TMDB. When a movie is clicked, details of the movie are displayed in a separate page.

The list of popular movies is fetched from:
https://developers.themoviedb.org/3/movies/get-popular-movies

And details of each movie are fetched from:
https://developers.themoviedb.org/3/movies/get-movie-details

Tech stacked used:
* Kotlin + Coroutines + Flows
* MVVM + Repository pattern + Clean Architecture + SOLID principles
* Dependency Injection via Hilt
* Jetpack Paging 3 library
* Jetpack Compose + Navigation
* Coil for image loading
* TDD approach (Wrote 23 unit tests for now. Might add integration, UI and end-to-end tests later)
