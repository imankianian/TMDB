package com.example.tmdb.di

import androidx.paging.PagingSource
import com.example.tmdb.data.repository.Repository
import com.example.tmdb.data.repository.RepositoryImpl
import com.example.tmdb.data.repository.TMDBPagingSource
import com.example.tmdb.data.repository.remote.datasource.RemoteDataSource
import com.example.tmdb.data.repository.remote.datasource.RemoteDataSourceImpl
import com.example.tmdb.data.repository.remote.model.Movie
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractModules {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindPagingSource(tMDBPagingSource: TMDBPagingSource): PagingSource<Int, Movie>

    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}