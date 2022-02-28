package com.madalinapreda.photosgallery.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.madalinapreda.photosgallery.model.Photo
import com.madalinapreda.photosgallery.repository.GalleryRepository
import com.madalinapreda.photosgallery.GalleryApplication
import com.madalinapreda.photosgallery.helpers.NetworkHelper
import kotlinx.coroutines.launch
import java.lang.Exception

class PhotosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GalleryRepository
    private val networkHelper: NetworkHelper

    val apiResponseLiveData = MutableLiveData<List<Photo>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Exception?>()
    val connectivityLiveData: MutableLiveData<Boolean>
        get() {
            return networkHelper.isConnectedLiveData
        }

    init {
        val galleryApp = application as GalleryApplication
        repository = galleryApp.getGalleryRepository()
        networkHelper = galleryApp.getNetworkHelper()
    }

    fun getPhotosInAlbum(albumId: Int) {
        viewModelScope.launch {
            try {
                loadingLiveData.value = true

                val havePhotosSaved = repository.getPhotosCountForAlbum(albumId) > 0

                val photos: ArrayList<Photo>
                if (havePhotosSaved) {
                    // Have cache => fetching from the DB
                    photos = repository.getPhotosForAlbum(albumId) as ArrayList
                } else {
                    // No cache => fetching from the API (& saving)
                    photos = repository.getPhotosInAlbumFromApi(albumId)
                    repository.savePhotos(photos)
                }
                loadingLiveData.value = false
                errorLiveData.value = null
                apiResponseLiveData.value = photos

            } catch (exception: Exception) {
                Log.d("PhotosViewModel", "exception thrown! --> ${exception.localizedMessage}")
                loadingLiveData.value = false
                errorLiveData.value = exception
            }
        }
    }
}