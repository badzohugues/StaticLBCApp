package com.badzohugues.staticlbcapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.badzohugues.staticlbcapp.MainCoroutinesRule
import com.badzohugues.staticlbcapp.data.repository.FakeAlbumItemRepository
import com.badzohugues.staticlbcapp.data.repository.Repository
import com.badzohugues.staticlbcapp.getOrAwaitValue
import com.badzohugues.staticlbcapp.misc.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    // Required to test LiveData
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Required to use Coroutines
    @get:Rule
    var mainCoroutinesRule = MainCoroutinesRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: Repository
    private lateinit var testCoroutineDispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        testCoroutineDispatcher = TestCoroutineDispatcher()
        repository = FakeAlbumItemRepository(dispatcher = testCoroutineDispatcher)
        viewModel = HomeViewModel(repository, 42)
    }

    @Test
    fun `getAlbums when api return error`() {
        runBlockingTest {
            // to simulate error from api
            (repository as FakeAlbumItemRepository).toForceNetworkError(true)
            viewModel.getAlbums(true)
            val result = viewModel.albums.getOrAwaitValue()
            assertThat(result.status).isEqualTo(Status.ERROR)
        }
    }

    @Test
    fun `getAlbumsAlbumAsync return success and album correctly saved in database`() {
        runBlockingTest {
            viewModel.getAlbums(true)
            val result = viewModel.albums.getOrAwaitValue()
            assertThat(result.status).isEqualTo(Status.SUCCESS)
            assertThat(result.data).isNotEmpty()
        }
    }
}
