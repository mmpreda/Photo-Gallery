package com.madalinapreda.photosgallery.network

import com.madalinapreda.photosgallery.model.Album
import com.madalinapreda.photosgallery.model.Photo
import com.madalinapreda.photosgallery.model.User
import com.madalinapreda.photosgallery.network.GalleryApiService.Companion.QUERY_ALBUMS
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryApi {

    @GET("albums")
    suspend fun getAlbums(): ArrayList<Album>

    @GET("users")
    suspend fun getUsers(): ArrayList<User>

    @GET("photos")
    suspend fun getPhotosInAlbum(@Query(QUERY_ALBUMS) albumId: Int): ArrayList<Photo>
}