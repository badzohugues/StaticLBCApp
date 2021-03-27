package com.badzohugues.staticlbcapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.badzohugues.staticlbcapp.MainCoroutinesRule
import com.badzohugues.staticlbcapp.data.repository.FakeAlbumItemRepository
import com.badzohugues.staticlbcapp.getOrAwaitValue
import com.badzohugues.staticlbcapp.misc.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `getAlbums from Api`() {
        /*viewModel.getAllAlbumItemFromApi()
        val result = viewModel.albums().getOrAwaitValue()
        assertThat(result.status).isEqualTo(Status.ERROR)*/
    }

    /*@Test
    fun `getAlbums from Database`() {
        viewModel.getAlbums(true)
        val result = viewModel.albums().getOrAwaitValue()
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `get items of album`() {
        viewModel.getItemsOfAlbum(12)
        val result = viewModel.albumItems().getOrAwaitValue()
        assertThat(result.status).isEqualTo(Status.ERROR)
    }*/
}