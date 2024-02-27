package com.medo.welcome.base

import com.medo.common.di.CoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseTest {
    protected val coroutineDispatchers = CoroutineDispatchers(
        main = UnconfinedTestDispatcher(),
        io = UnconfinedTestDispatcher(),
    )
}