package com.example.trainingmanager.di

import com.example.trainingmanager.repository.AppRepository
import com.example.trainingmanager.repository.AppRepositoryImpl
import com.example.trainingmanager.service.AppService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppRepository(appService: AppService): AppRepository {
        return AppRepositoryImpl(appService)
    }

}