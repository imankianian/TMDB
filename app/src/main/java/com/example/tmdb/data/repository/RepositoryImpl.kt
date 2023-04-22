package com.example.tmdb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.tmdb.PAGE_SIZE
import com.example.tmdb.data.repository.remote.datasource.RemoteDataSource
import com.example.tmdb.data.repository.remote.model.Movie
import com.example.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val pagingSource: PagingSource<Int, Movie>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
    ): Repository {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { pagingSource }
        ).flow
    }

    override suspend fun getMovieDetails(movieId: Int) = withContext(dispatcher) {
        remoteDataSource.fetchMovieDetails(movieId)
    }
}