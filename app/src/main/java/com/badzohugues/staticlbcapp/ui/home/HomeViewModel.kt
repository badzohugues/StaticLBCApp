package com.badzohugues.staticlbcapp.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.badzohugues.staticlbcapp.data.db.room.StaticLBCDatabase
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.repository.AlbumItemRepository
import com.badzohugues.staticlbcapp.misc.DataWrapper
import kotlinx.coroutines.*

private const val TAG = "HomeViewModel"
private const val TIMEOUT = 7000L
private const val SHORT_TIMEOUT = 5000L

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val _albums = MutableLiveData<DataWrapper<List<AlbumItem>>>()
    private val _albumItems = MutableLiveData<DataWrapper<List<AlbumItem>>>()
    private val repository : AlbumItemRepository
    private var lastAlbumId = 0
    private var isExceptionFromServer = false

    init {
        val albumItemDao = StaticLBCDatabase.getDatabase(application).albumItemDao()
        repository = AlbumItemRepository(albumItemDao)
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG,"Exception thrown somewhere within parent or child: $exception.")
        if (isExceptionFromServer) {    // in case we can't reach server we get data from database
            getAlbums(false)
            Log.d(TAG,"Trying to get data from database")
            !isExceptionFromServer
        }
    }


    private fun updateAlbumsDatas(allAlbums: DataWrapper<List<AlbumItem>>) {
        _albums.value = allAlbums
    }

    private fun updateItemsOfAlbum(itemsOfAlbum: DataWrapper<List<AlbumItem>>, currentAlbumId: Int) {
        _albumItems.value = itemsOfAlbum
        lastAlbumId = currentAlbumId
    }

    fun albums(): LiveData<DataWrapper<List<AlbumItem>>> = _albums

    fun albumItems(): LiveData<DataWrapper<List<AlbumItem>>> = _albumItems

    fun getAlbums(isConnected : Boolean) {
        val getAlbumsJob = viewModelScope.launch(handler) {
            val jobAllAlbumItems = async(start = CoroutineStart.LAZY) {
                isExceptionFromServer = true
                repository.fetchAllAlbumItem()
            }
            val jobGetAlbumsOffline = async(start = CoroutineStart.LAZY) { repository.getAlbums(isConnected) }

            withTimeout(TIMEOUT) {
                supervisorScope {
                    var allAlbums: List<AlbumItem> = emptyList()
                    val isDataEmpty = _albums.value?.data.isNullOrEmpty() // if data already showed in UI

                    if(isConnected && isDataEmpty) {
                        _albums.value = DataWrapper.loading(emptyList())

                        // Because we can display data even if there's an exception with insertion in database
                        val jobSaveAllAlbumItems = launch {
                            allAlbums = jobAllAlbumItems.await()
                            isExceptionFromServer = false
                            repository.saveAllAlbumItems(allAlbums)
                        }
                        jobSaveAllAlbumItems.join()
                        updateAlbumsDatas(DataWrapper.success(allAlbums.distinctBy { it.albumId }))
                    }
                    else if (isDataEmpty) {
                        _albums.value = DataWrapper.loading(emptyList())
                        updateAlbumsDatas(DataWrapper.success(jobGetAlbumsOffline.await()))
                    }
                }
            }
        }

        // We catch exception here in last layer
        getAlbumsJob.invokeOnCompletion { throwable ->
            if(throwable != null) {
                Log.d(TAG,"getAlbums $throwable")
                _albums.value = DataWrapper.error(throwable.message, emptyList())
            }
            else {
                Log.d(TAG,"update due to throwable")
                updateAlbumsDatas(DataWrapper.success(_albums.value?.data ?: emptyList()))
            }
        }
    }

    fun getItemsOfAlbum(albumId: Int) {
        val getItemsOfAlbumJob = viewModelScope.launch(handler) {
            val jobGetItemsOfAlbum = async(start = CoroutineStart.LAZY) { repository.getItemsOfAlbum(albumId) }
            withTimeout(SHORT_TIMEOUT) {
                if (albumId != lastAlbumId) {
                    _albumItems.value = DataWrapper.loading(emptyList())
                    updateItemsOfAlbum(DataWrapper.success(jobGetItemsOfAlbum.await()), albumId)
                }
            }
        }

        getItemsOfAlbumJob.invokeOnCompletion { throwable ->
            if(throwable != null) {
                Log.d(TAG, "getItemsOfAlbum $throwable")
                _albumItems.value = DataWrapper.error(throwable.message, emptyList())
            }
            else {
                Log.d(TAG,"update due to throwable")
                updateItemsOfAlbum(DataWrapper.success(_albumItems.value?.data ?: emptyList()), lastAlbumId)
            }
        }
    }
}