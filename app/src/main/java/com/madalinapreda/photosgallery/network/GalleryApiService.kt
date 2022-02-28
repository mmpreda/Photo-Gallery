package com.madalinapreda.photosgallery.network

import androidx.annotation.VisibleForTesting
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GalleryApiService {

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
    }.build()

    @VisibleForTesting
    /*private*/ val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
//        .client(okHttpClient)
        .build()
        .create(GalleryApi::class.java)

    suspend fun getAlbums() = api.getAlbums()

    suspend fun getUsers() = api.getUsers()

    suspend fun getPhotosInAlbum(albumId: Int) = api.getPhotosInAlbum(albumId)

    companion object {
        const val BASE_URL: String = "https://jsonplaceholder.typicode.com/"
        const val QUERY_ALBUMS: String = "albumId"
    }
}