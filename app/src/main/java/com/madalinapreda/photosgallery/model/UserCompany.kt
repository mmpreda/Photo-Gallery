package com.madalinapreda.photosgallery.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserCompany(
    @SerializedName("name")
    val companyName: String,
    val catchPhrase: String,
    @SerializedName("bs")
    val slogan: String
) : Parcelable
