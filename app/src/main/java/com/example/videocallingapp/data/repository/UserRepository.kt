package com.example.videocallingapp.data.repository

import com.example.videocallingapp.data.model.User
import com.example.videocallingapp.ui.login.sign_in.UserData
import com.example.videocallingapp.util.UiState

interface UserRepository {
    fun getUsersData(result: (UiState<List<User>>) -> Unit)
}