package com.example.videocallingapp.ui.login

import androidx.lifecycle.ViewModel
import com.example.videocallingapp.ui.login.sign_in.SignInResult
import com.example.videocallingapp.ui.login.sign_in.SignInState
import com.example.videocallingapp.ui.login.sign_in.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}