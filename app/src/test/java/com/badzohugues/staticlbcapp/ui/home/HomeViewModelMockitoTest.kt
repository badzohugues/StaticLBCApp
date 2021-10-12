package com.badzohugues.staticlbcapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.badzohugues.staticlbcapp.MainCoroutinesRule
import com.badzohugues.staticlbcapp.data.api.datasource.ApiDatasource
import com.badzohugues.staticlbcapp.data.api.service.AlbumItemApiService
import com.badzohugues.staticlbcapp.data.db.datasource.DbDatasource
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.repository.AlbumItemRepository
import com.badzohugues.staticlbcapp.getOrAwaitValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeViewModelMockitoTest {
    // Required to test LiveData
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Required to use Coroutines
    @get:Rule
    var mainCoroutinesRule = MainCoroutinesRule()

    @Mock
    private lateinit var albumItemService: AlbumItemApiService
    @Mock
    private lateinit var dbDatasource: DbDatasource
    private lateinit var apiDatasource: ApiDatasource
    private lateinit var repository: AlbumItemRepository
    private lateinit var viewModel: HomeViewModel
    private lateinit var testCoroutineDispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        testCoroutineDispatcher = TestCoroutineDispatcher()
        albumItemService = Mockito.mock(AlbumItemApiService::class.java)
        dbDatasource = Mockito.mock(DbDatasource::class.java)
        apiDatasource = ApiDatasource(albumItemService)
        repository = AlbumItemRepository(
            dbDatasource,
            apiDatasource,
            testCoroutineDispatcher
        )
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `getAlbums when api return success`() {

        val type = object : TypeToken<Response<List<AlbumItem>>>() {}.type
        val bodyResponse: List<AlbumItem> = Gson().fromJson(
            "album_items_response.json",
            javaClass,
            type
        )

        runBlockingTest {
            Mockito.`when`(albumItemService.fetchAllAlbumItems())
                .thenReturn()
            viewModel.getAlbums(true)
            val result = viewModel.albums.getOrAwaitValue()
        }
    }
}
