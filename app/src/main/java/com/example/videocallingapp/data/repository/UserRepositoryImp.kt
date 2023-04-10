package com.example.videocallingapp.data.repository


import com.example.videocallingapp.data.model.User
import com.example.videocallingapp.ui.login.sign_in.UserData
import com.example.videocallingapp.util.UiState
import com.google.firebase.database.FirebaseDatabase

class UserRepositoryImp(
    private val database: FirebaseDatabase
) : UserRepository {

    override fun getUsersData(result: (UiState<List<User>>) -> Unit) {
        val reference = database.getReference("Users")
        reference.get()
            .addOnSuccessListener {
                val tasks = arrayListOf<User?>()
                for (item in it.children){
                    val task = item.getValue(User::class.java)
                    tasks.add(task)
                }
                result.invoke(UiState.Success(tasks.filterNotNull()))
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }
}