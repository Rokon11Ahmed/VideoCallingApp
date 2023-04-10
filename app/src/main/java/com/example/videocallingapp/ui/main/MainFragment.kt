package com.example.videocallingapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videocallingapp.R
import com.example.videocallingapp.data.model.User
import com.example.videocallingapp.databinding.FragmentMainBinding
import com.example.videocallingapp.extention.milliSecondToTime
import com.example.videocallingapp.ui.login.sign_in.GoogleAuthUiClient
import com.example.videocallingapp.util.UiState
import com.example.videocallingapp.util.sortByTime
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()
    //private var agoraView: AgoraVideoViewer? = null

    private val adapter by lazy {
        UserListAdapter(
            onItemClicked = { pos, item -> onItemClicked(pos, item) }
        )
    }

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext(),
            oneTapClient = Identity.getSignInClient(requireActivity())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            userListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            userListRecyclerView.adapter = adapter

            fabLive.setOnClickListener {
                // need to initialize agora-io video call API
                Toast.makeText(requireContext(), getText(R.string.IDS_FEATURE_COMING), Toast.LENGTH_SHORT).show()
            }

            logoutImageView.setOnClickListener {
                Firebase.auth.signOut()
                val navOption = NavOptions.Builder()
                    .setPopUpTo(R.id.mainFragment, true)
                    .build()
                findNavController().navigate(R.id.loginFragment, null, navOption)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getUsers()
            }
        }

        viewModel.getUsers()
        startObserver()

    }

    private fun startObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.users.observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is UiState.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            adapter.updateList(sortByTime(state.data).toMutableList())
                        }
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun onItemClicked(position : Int, user: User) {
        // need to join video call by user room id
        Toast.makeText(requireContext(), getText(R.string.IDS_FEATURE_COMING), Toast.LENGTH_SHORT).show()
    }

}