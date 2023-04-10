package com.example.videocallingapp.ui.login.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?,
    val lastLoginTime: Long = System.currentTimeMillis(),
    val isOnLive: Boolean = false
)
