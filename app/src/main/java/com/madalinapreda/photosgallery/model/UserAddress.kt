package com.madalinapreda.photosgallery.model

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAddress(
    val street: String,
    val suite: String,
    val city: String,
    @SerializedName("zipcode")
    val zipCode: String,
    @Embedded
    // This field is Nullable only for test purposes
    val geo: Geo?
): Parcelable {

    @Parcelize
    data class Geo(val lat: Float,
                   val lng: Float): Parcelable
}
