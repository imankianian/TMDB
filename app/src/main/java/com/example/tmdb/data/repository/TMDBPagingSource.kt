package com.example.tmdb.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdb.MoviesResult
import com.example.tmdb.PAGE_SIZE
import com.example.tmdb.STARTING_PAGE_INDEX
import com.example.tmdb.data.repository.remote.datasource.RemoteDataSource
import com.example.tmdb.data.repository.remote.model.Movie
import com.example.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TMDBPagingSource @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
    ): PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>) = withContext(dispatcher) {
        val position = params.key ?: STARTING_PAGE_INDEX
        when (val response = remoteDataSource.fetchPopularMovies(position)) {
            is MoviesResult.Success -> {
                val nextKey = if (response.movieResponse.movies.isEmpty()) {
                    null
                } else {
                    position + (params.loadSize / PAGE_SIZE)
                }
                LoadResult.Page(
                    data = response.movieResponse.movies,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = nextKey
                )
            }
            is MoviesResult.Error -> {
                LoadResult.Error(Throwable("${response.code}: ${response.message}"))
            }
            is MoviesResult.Failure -> {
                LoadResult.Error(Throwable(response.message))
            }
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}