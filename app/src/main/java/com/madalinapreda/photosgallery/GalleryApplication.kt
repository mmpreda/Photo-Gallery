package com.madalinapreda.photosgallery

import android.app.Application
import com.madalinapreda.photosgallery.database.GalleryDatabase
import com.madalinapreda.photosgallery.helpers.NetworkHelper
import com.madalinapreda.photosgallery.repository.GalleryRepository

class GalleryApplication : Application() {

    private val database by lazy {
        GalleryDatabase.getDatabase(this@GalleryApplication)
    }

    private var galleryRepository: GalleryRepository? = null

    fun getGalleryRepository(): GalleryRepository {
        if (galleryRepository == null) {
            return GalleryRepository(database.getDao())
        }
        return galleryRepository!!
    }

    private var networkHelper: NetworkHelper? = null

    fun getNetworkHelper() : NetworkHelper {
        if (networkHelper == null) {
            val helper = NetworkHelper(this)
            helper.start()
            return helper
        }
        networkHelper!!.start()
        return networkHelper!!
    }
}