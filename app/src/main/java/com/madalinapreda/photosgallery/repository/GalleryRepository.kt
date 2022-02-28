package com.madalinapreda.photosgallery.repository

import androidx.annotation.WorkerThread
import com.madalinapreda.photosgallery.database.GalleryDao
import com.madalinapreda.photosgallery.model.Album
import com.madalinapreda.photosgallery.model.Photo
import com.madalinapreda.photosgallery.model.User
import com.madalinapreda.photosgallery.network.GalleryApiService
import kotlinx.coroutines.flow.Flow

class GalleryRepository(private val galleryDao: GalleryDao) {

    //region API

    private val apiService = GalleryApiService()

    suspend fun getAlbumsFromApi() = apiService.getAlbums()

    suspend fun getUsersFromApi() = apiService.getUsers()

    suspend fun getPhotosInAlbumFromApi(albumId: Int) = apiService.getPhotosInAlbum(albumId)

    //endregion

    //region Database

    //region Users

    @WorkerThread
    suspend fun saveUsers(users: ArrayList<User>) {
        galleryDao.insertUsers(users)
    }

    @WorkerThread
    suspend fun getUsersCount() = galleryDao.getUsersCount()

    //endregion

    //region Albums

    @WorkerThread
    suspend fun saveAlbums(albums: ArrayList<Album>) {
        galleryDao.insertAlbums(albums)
    }

    @WorkerThread
    suspend fun getAlbumsCount() = galleryDao.getAlbumsCount()

    val albumsWithAuthors: Flow<Map<Album, String>> = galleryDao.getAlbumsWithAuthors()

    //endregion

    //region Photos

    @WorkerThread
    suspend fun savePhotos(photos: ArrayList<Photo>) {
        galleryDao.insertPhotos(photos)
    }

    @WorkerThread
    suspend fun getPhotosCountForAlbum(albumId: Int) = galleryDao.getPhotosCountForAlbum(albumId)

    @WorkerThread
    suspend fun getPhotosForAlbum(albumId: Int) = galleryDao.getPhotosForAlbum(albumId)

    //endregion

    //endregion
}