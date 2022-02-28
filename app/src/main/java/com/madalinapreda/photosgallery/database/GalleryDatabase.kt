package com.madalinapreda.photosgallery.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.madalinapreda.photosgallery.model.Album
import com.madalinapreda.photosgallery.model.Photo
import com.madalinapreda.photosgallery.model.User

@Database(entities = [User::class, Album::class, Photo::class], version = 1)
abstract class GalleryDatabase : RoomDatabase() {

    abstract fun getDao(): GalleryDao

    companion object {
        private const val DATABASE_NAME = "gallery_database.db"

        @Volatile
        private var INSTANCE: GalleryDatabase? = null

        fun getDatabase(context: Context): GalleryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, GalleryDatabase::class.java, DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}