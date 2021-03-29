package com.badzohugues.staticlbcapp.ui.home

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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _albums = MutableLiveData<ResultWrapper<List<AlbumItem>>>()
    private val _itemsOfAlbum = MutableLiveData<ResultWrapper<List<AlbumItem>>>()
    private var lastAlbumId = 0

    private fun updateAlbumsDatas(allAlbums: ResultWrapper<List<AlbumItem>>) {
        _albums.value = allAlbums
    }

    private fun updateItemsOfAlbum(
        itemsOfAlbum: ResultWrapper<List<AlbumItem>>,
        currentAlbumId: Int
    ) {
        _itemsOfAlbum.value = itemsOfAlbum
        lastAlbumId = currentAlbumId
    }

    val albums: LiveData<ResultWrapper<List<AlbumItem>>> get() = _albums

    fun itemsOfAlbum(): LiveData<ResultWrapper<List<AlbumItem>>> = _itemsOfAlbum

    fun getAlbums(isConnected: Boolean) {
        viewModelScope.launch {
            if (_albums.value?.data.isNullOrEmpty()) {
                if (isConnected) {
                    _albums.value = ResultWrapper.loading(emptyList())
                    updateAlbumsDatas(repository.getAlbumsAsync().await())
                } else {
                    var offlineAlbums: List<AlbumItem>
                    withContext(Dispatchers.IO) {
                        offlineAlbums = repository.getAlbums()
                    } // to read data from database we need to use IO Dispatchers

                    if (!offlineAlbums.isNullOrEmpty()) updateAlbumsDatas(
                        ResultWrapper.success(
                            offlineAlbums
                        )
                    )
                }
            }
        }
    }

    fun getItemsOfAlbum(albumId: Int) {
        viewModelScope.launch {
            val jobGetItemsOfAlbum = async(start = CoroutineStart.LAZY) {
                withContext(Dispatchers.IO) { repository.getItemsOfAlbum(albumId) }
            }
            if (albumId != lastAlbumId) {
                _itemsOfAlbum.value = ResultWrapper.loading(emptyList())
                updateItemsOfAlbum(ResultWrapper.success(jobGetItemsOfAlbum.await()), albumId)
            }
        }
    }
}