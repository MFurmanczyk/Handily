package com.handily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import com.handily.model.User
import com.handily.repository.FirestoreProvider

class UserDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val _authenticatedUser = MutableLiveData<User>()
    val authenticatedUser: LiveData<User>
        get() = _authenticatedUser

    private var firestoreProvider = FirestoreProvider.instance

    fun setUser(uuid: String, isFirstLogin: Boolean) {
        if(isFirstLogin) {
            val user = User.Builder(uuid, Firebase.auth.currentUser?.email.toString()).build()
            _authenticatedUser.postValue(user)
        } else {
            firestoreProvider.getUser(uuid) {
                it?.let { _authenticatedUser.postValue(it) }
            }
        }
    }

    /**
     * @param user - user with proper uuid to be saved
     * @param isFresh - [true] if user is being saved first time, [false] if user is being updated
     */

    fun saveUser(user: User, isFresh: Boolean) {
        if(isFresh) {
            firestoreProvider.addUser(user)
        } else {
            firestoreProvider.updateUser(user)
        }

        _authenticatedUser.postValue(user)
    }
}