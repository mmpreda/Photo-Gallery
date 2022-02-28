package com.madalinapreda.photosgallery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.madalinapreda.photosgallery.GalleryApplication
import com.madalinapreda.photosgallery.helpers.NetworkHelper
import com.madalinapreda.photosgallery.model.Album
import com.madalinapreda.photosgallery.repository.GalleryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GalleryRepository
    private val networkHelper: NetworkHelper

    val errorMessageLiveData = MutableLiveData<Exception?>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val connectivityLiveData: MutableLiveData<Boolean>
        get() {
            return networkHelper.isConnectedLiveData
        }

    init {
        val galleryApp = application as GalleryApplication
        repository = galleryApp.getGalleryRepository()
        networkHelper = galleryApp.getNetworkHelper()
    }

    fun prepareAlbumsData() {
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            withContext(Dispatchers.IO) {
                try {
                    val isConnected = connectivityLiveData.value
                    val usersJob = async { fetchUsersData(isConnected!!) }
                    val albumsJob = async { fetchAlbumsData(isConnected!!) }
                    val fetchedAlbums = albumsJob.await()

                    if (fetchedAlbums != null) {
                        usersJob.await()
                        repository.saveAlbums(fetchedAlbums)
                    } else {
                        // Nothing in cache and API call failed
                        errorMessageLiveData.value =
                            IllegalStateException("Impossible to get any data")
                    }
                    loadingLiveData.postValue(false)

                } catch (exception: Exception) {
                    loadingLiveData.postValue(false)
                    errorMessageLiveData.postValue(exception)
                    this.cancel("${exception.cause}", exception)
                }
            }
        }
    }

    private suspend fun fetchUsersData(isConnected: Boolean) {
        val hasUsersSaved = repository.getUsersCount() > 0
        if (!hasUsersSaved && isConnected) {
            val response = repository.getUsersFromApi()
            repository.saveUsers(response)
        }
    }

    private suspend fun fetchAlbumsData(isConnected: Boolean): ArrayList<Album>? {
        val hasAlbumsSaved = repository.getAlbumsCount() > 0
        if (!hasAlbumsSaved && isConnected) {
            return repository.getAlbumsFromApi()
        }
        return null
    }

    fun getAlbumsWithAuthors(): LiveData<Map<Album, String>> {
        loadingLiveData.postValue(true)
        val result = repository.albumsWithAuthors.asLiveData()
        loadingLiveData.postValue(false)
        return result
    }

    class AlbumsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}