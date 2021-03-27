package com.badzohugues.staticlbcapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.repository.Repository
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

private const val TAG = "HomeViewModel"
private const val TIMEOUT = 7000L

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
    ): ViewModel() {

    private val _albums = MutableLiveData<ResultWrapper<List<AlbumItem>>>()
    private val _itemsOfAlbum = MutableLiveData<ResultWrapper<List<AlbumItem>>>()
    private var lastAlbumId = 0
    private var isExceptionFromServer = false

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG, "Exception thrown somewhere within parent or child: $exception.")
        if (isExceptionFromServer) {    // in case we can't reach data from server we get data from database
            getAlbums(false)
            Log.d(TAG, "Trying to get data from database")
            !isExceptionFromServer
        }
    }

    private fun updateAlbumsDatas(allAlbums: ResultWrapper<List<AlbumItem>>) {
        _albums.value = allAlbums
    }

    private fun updateItemsOfAlbum(itemsOfAlbum: ResultWrapper<List<AlbumItem>>, currentAlbumId: Int) {
        _itemsOfAlbum.value = itemsOfAlbum
        lastAlbumId = currentAlbumId
    }

    fun albums(): LiveData<ResultWrapper<List<AlbumItem>>> = _albums

    fun itemsOfAlbum(): LiveData<ResultWrapper<List<AlbumItem>>> = _itemsOfAlbum

    fun getAlbums(isConnected : Boolean) {
        viewModelScope.launch(handler) {
            val jobAllAlbumItems = async(start = CoroutineStart.LAZY) {
                isExceptionFromServer = true
                repository.fetchAllAlbumItem()
            }
            val jobGetAlbumsOffline = async(start = CoroutineStart.LAZY) {
                withContext(Dispatchers.IO) { repository.getAlbums() }
            }

            withTimeout(7000L) {
                supervisorScope {
                    var allAlbums: List<AlbumItem> = emptyList()
                    val isDataEmpty = _albums.value?.data.isNullOrEmpty() // if data already shew by UI

                    if(isConnected && isDataEmpty) {
                        _albums.value = ResultWrapper.loading(emptyList())

                        val jobSaveAllAlbumItems = launch {
                            allAlbums = jobAllAlbumItems.await()
                            isExceptionFromServer = false
                            repository.saveAllAlbumItems(allAlbums)
                        }
                        jobSaveAllAlbumItems.join()
                        updateAlbumsDatas(ResultWrapper.success(allAlbums.distinctBy { it.albumId }))
                    }
                    else if (isDataEmpty) {
                        _albums.value = ResultWrapper.loading(emptyList())
                        updateAlbumsDatas(ResultWrapper.success(jobGetAlbumsOffline.await()))
                    }
                }
            }
        }.invokeOnCompletion { throwable ->
            if(throwable != null) _albums.value = ResultWrapper.error(throwable.message, emptyList())
            else updateAlbumsDatas(ResultWrapper.success(_albums.value?.data))
        }
    }

    fun getItemsOfAlbum(albumId: Int) {
        viewModelScope.launch {
            val jobGetItemsOfAlbum = async(start = CoroutineStart.LAZY) { withContext(Dispatchers.IO) { repository.getItemsOfAlbum(albumId) } }
            if (albumId != lastAlbumId) {
                _itemsOfAlbum.value = ResultWrapper.loading(emptyList())
                updateItemsOfAlbum(ResultWrapper.success(jobGetItemsOfAlbum.await()), albumId)
            }
        }
    }
}