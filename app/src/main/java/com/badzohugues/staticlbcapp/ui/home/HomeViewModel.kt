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

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val _albumItems = MutableLiveData<DataWrapper<List<AlbumItem>>>()
    private val repository : AlbumItemRepository

    init {
        val albumItemDao = StaticLBCDatabase.getDatabase(application).albumItemDao()
        repository = AlbumItemRepository(albumItemDao)
    }

    fun albumItems(): LiveData<DataWrapper<List<AlbumItem>>> = _albumItems

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG,"Exception thrown somewhere within parent or child: $exception.")
    }

    private fun updateAlbumsDatas(allAlbums: List<AlbumItem>) {
        _albumItems.value = DataWrapper.success(allAlbums)
    }

    fun getAlbums(isConnected : Boolean) {
        val parentJob = viewModelScope.launch(handler) {
            val jobAllAlbumItems = async(start = CoroutineStart.LAZY) { repository.fetchAllAlbumItem() }
            val jobGetAlbumsOffline = async(start = CoroutineStart.LAZY) { repository.getAlbums(isConnected) }

            withTimeout(TIMEOUT) {
                supervisorScope {
                    var allAlbums: List<AlbumItem> = emptyList()
                    val isDataEmpty = _albumItems.value?.data.isNullOrEmpty() // if data already showed in UI

                    if(isConnected && isDataEmpty) {
                        _albumItems.value = DataWrapper.loading(emptyList()) // to display progress bar on UI

                        // Because we can display data even if there's an exception with insertion in database
                        val jobSaveAllAlbumItems = launch {
                            allAlbums = jobAllAlbumItems.await()
                            repository.saveAllAlbumItems(allAlbums)
                        }
                        jobSaveAllAlbumItems.join()
                        updateAlbumsDatas(allAlbums.distinctBy { it.albumId })
                    }
                    else if (isDataEmpty) {
                        _albumItems.value = DataWrapper.loading(emptyList())
                        updateAlbumsDatas(jobGetAlbumsOffline.await())
                    }
                }
            }
        }

        parentJob.invokeOnCompletion { throwable ->
            if(throwable != null) {
                Log.d(TAG,"Parent job failed: $throwable")
                _albumItems.value = DataWrapper.error(throwable.message, emptyList())
            }
            else {
                Log.d(TAG,"Parent job SUCCESS")
                updateAlbumsDatas(_albumItems.value?.data ?: emptyList())
            }
        }
    }
}