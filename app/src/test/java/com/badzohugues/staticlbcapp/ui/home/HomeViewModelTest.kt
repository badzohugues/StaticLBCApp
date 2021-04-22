package com.badzohugues.staticlbcapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.badzohugues.staticlbcapp.MainCoroutinesRule
import com.badzohugues.staticlbcapp.data.repository.FakeAlbumItemRepository
import com.badzohugues.staticlbcapp.data.repository.Repository
import com.badzohugues.staticlbcapp.getOrAwaitValue
import com.badzohugues.staticlbcapp.misc.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Before
    fun setup() {
        repository = FakeAlbumItemRepository()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `getAlbums when api return error`() {
        runBlockingTest {
            (repository as FakeAlbumItemRepository).toForceNetworkError(true) // to simulate error from api
            viewModel.getAlbums(true)
            var result = viewModel.albums.getOrAwaitValue()
            assertThat(result.status).isEqualTo(Status.LOADING)
            result = viewModel.albums.getOrAwaitValue()
            assertThat(result.status).isEqualTo(Status.ERROR)
        }
    }

    @Test
    fun `getAlbumsAlbumAsync return success and album correctly saved in database`() {
        runBlockingTest {
            viewModel.getAlbums(true)
            var result = viewModel.albums.getOrAwaitValue()
            assertThat(result.status).isEqualTo(Status.LOADING)
            result = viewModel.albums.getOrAwaitValue()
            assertThat(result.status).isEqualTo(Status.SUCCESS)
            assertThat(result.data).isNotEmpty()
        }
    }
}