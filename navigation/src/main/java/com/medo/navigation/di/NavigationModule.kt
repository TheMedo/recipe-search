package com.medo.navigation.di

import com.medo.navigation.ComposeNavigationController
import com.medo.navigation.NavigationController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindNavigationController(controller: ComposeNavigationController): NavigationController
}