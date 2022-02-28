package com.madalinapreda.photosgallery.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "albums",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"]
    )],
    indices = [Index("user_id")]
)
data class Album(
    @ColumnInfo(name = "user_id") val userId: Int,
    @PrimaryKey
    @ColumnInfo(name = "album_id") val id: Int,
    val title: String
) : Parcelable