package com.medo.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class CoroutineDispatchers(
    val main: CoroutineDispatcher = Dispatchers.Main,
    val io: CoroutineDispatcher = Dispatchers.IO,
)

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchers()
}