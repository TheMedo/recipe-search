package com.medo.navigation.di

import com.medo.navigation.ComposeNavigationController
import com.medo.navigation.NavigationController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    fun provideNavigationController(): NavigationController = ComposeNavigationController()
}