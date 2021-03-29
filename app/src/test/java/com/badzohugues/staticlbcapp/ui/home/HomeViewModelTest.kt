package com.badzohugues.staticlbcapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.badzohugues.staticlbcapp.MainCoroutinesRule
import com.badzohugues.staticlbcapp.data.repository.FakeAlbumItemRepository
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
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutinesRule = MainCoroutinesRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(FakeAlbumItemRepository())
    }

    @Test
    fun `getAlbums when api return error`() {
        runBlockingTest {
            viewModel.getAlbums(true)
        }
        val result = viewModel.albums.getOrAwaitValue()
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `getAlbums from Database`() {
        viewModel.getAlbums(false)
        val result = viewModel.albums.getOrAwaitValue()
        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `get items of album success`() {
        viewModel.getItemsOfAlbum(42)
        val result = viewModel.itemsOfAlbum().getOrAwaitValue()
        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }
}