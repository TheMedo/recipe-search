package com.medo.welcome.ui

import com.medo.data.repository.StorageKey
import com.medo.data.repository.StorageRepository
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import com.medo.welcome.base.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest : BaseTest() {

    private lateinit var viewModel: WelcomeViewModel

    private lateinit var navigationController: NavigationController
    private lateinit var storageRepository: StorageRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        navigationController = mock<NavigationController>()
        storageRepository = mock<StorageRepository>()

        viewModel = WelcomeViewModel(
            coroutineDispatchers,
            navigationController,
            storageRepository
        )
    }

    @Test
    fun `given welcome view - when get started is pressed - then write to storage`() = runTest {
        viewModel.onEvent(WelcomeEvent.GetStarted)

        verify(storageRepository).setBoolean(StorageKey.HasSeenWelcome, true)
    }

    @Test
    fun `given welcome view - when get started is pressed - then navigate to home`() = runTest {
        viewModel.onEvent(WelcomeEvent.GetStarted)

        verify(navigationController).navigateTo(Destination.Home)
    }

    @After
    fun cleanup() = Dispatchers.resetMain()
}