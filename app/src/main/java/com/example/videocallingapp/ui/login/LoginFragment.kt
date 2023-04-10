package com.example.videocallingapp.ui.login


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.videocallingapp.R
import com.example.videocallingapp.databinding.FragmentLoginBinding
import com.example.videocallingapp.ui.login.sign_in.GoogleAuthUiClient
import com.example.videocallingapp.ui.login.sign_in.UserData
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginFragment : BindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext(),
            oneTapClient = Identity.getSignInClient(requireActivity())
        )
    }
    private val viewModel: LoginViewModel by viewModels()
    private val database = Firebase.database
    private val myRef = database.getReference("Users")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (googleAuthUiClient.getSignedInUser() != null) {
            lifecycleScope.launch {
                googleAuthUiClient.getSignedInUser()?.let { userData ->
                    updateTimeData(userData)
                }
            }
        }else {
            observeState()
            binding.signInGoogleButton.visibility = View.VISIBLE
        }

        binding.signInGoogleButton.setOnClickListener {
            lifecycleScope.launch {
                val signInIntentSender = googleAuthUiClient.signIn()
                resultLauncher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    it.isSignInSuccessful.let {
                        googleAuthUiClient.getSignedInUser()?.let { userData ->
                            uploadData(userData)
                        }
                    }
                    it.signInError?.let { error ->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private suspend fun uploadData(userData: UserData) {
        binding.progressBar.visibility = View.VISIBLE
        myRef.child(userData.userId).setValue(userData).await()
        binding.progressBar.visibility = View.GONE
        goToMainFragment()
    }

    private suspend fun updateTimeData(userData: UserData){
        binding.progressBar.visibility = View.VISIBLE
        myRef.child(userData.userId).child("lastLoginTime").setValue(System.currentTimeMillis()).await()
        binding.progressBar.visibility = View.GONE
        goToMainFragment()
    }

    private fun goToMainFragment(){
        val navOption = NavOptions.Builder()
            .setPopUpTo(R.id.loginFragment, true)
            .build()
        findNavController().navigate(R.id.mainFragment, null, navOption)
        viewModel.resetState()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult =
                        googleAuthUiClient.signInWithIntent(intent = result.data ?: return@launch)
                    viewModel.onSignInResult(signInResult)
                }
            }
        }

}