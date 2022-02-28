package com.madalinapreda.photosgallery.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "photos")
data class Photo(
    val albumId: Int,
    @PrimaryKey
    @ColumnInfo(name = "photo_id") val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) : Parcelable