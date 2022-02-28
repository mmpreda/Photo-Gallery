package com.madalinapreda.photosgallery.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madalinapreda.photosgallery.model.Album
import com.madalinapreda.photosgallery.model.Photo
import com.madalinapreda.photosgallery.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: ArrayList<User>)

    @Query("SELECT COUNT(id) FROM users")
    suspend fun getUsersCount(): Int

    //------------------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbums(albums: ArrayList<Album>)

    @Query("SELECT COUNT(album_id) FROM albums")
    suspend fun getAlbumsCount(): Int

    @Query("SELECT albums.*, users.name FROM albums " +
            "INNER JOIN users ON albums.user_id = users.id " +
            "ORDER BY albums.title ASC")
    @MapInfo(valueColumn = "name")
    fun getAlbumsWithAuthors(): Flow<Map<Album, String>>

    //------------------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhotos(photos: ArrayList<Photo>)

    @Query("SELECT COUNT(photo_id) FROM photos WHERE albumId=:albumId")
    suspend fun getPhotosCountForAlbum(albumId: Int): Int

    @Query("SELECT * FROM photos WHERE albumId=:albumId")
    suspend fun getPhotosForAlbum(albumId: Int): List<Photo>
}