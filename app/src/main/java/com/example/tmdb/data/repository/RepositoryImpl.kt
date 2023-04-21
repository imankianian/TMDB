package com.example.tmdb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.tmdb.PAGE_SIZE
import com.example.tmdb.data.repository.remote.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val pagingSource: PagingSource<Int, Movie>
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
}