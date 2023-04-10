package com.example.videocallingapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videocallingapp.data.model.User
import com.example.videocallingapp.data.repository.UserRepository
import com.example.videocallingapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _users = MutableLiveData<UiState<List<User>>>()
    val users: LiveData<UiState<List<User>>>
        get() = _users

    fun getUsers() {
        _users.value = UiState.Loading
        repository.getUsersData {
            _users.value = it
        }
    }
}