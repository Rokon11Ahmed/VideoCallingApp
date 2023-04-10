package com.example.videocallingapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String = "",
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val lastLoginTime: Long = System.currentTimeMillis(),
    val onLive: Boolean = false
) : Parcelable
