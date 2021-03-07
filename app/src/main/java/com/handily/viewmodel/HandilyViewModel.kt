package com.handily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.handily.model.User
import com.handily.repository.FirestoreProvider

class HandilyViewModel(application: Application): AndroidViewModel(application) {

    private val _authenticatedUser = MutableLiveData<User>()
    val authenticatedUser: LiveData<User>
        get() = _authenticatedUser

    private val firestoreProvider by lazy { FirestoreProvider.instance }

    fun setUser(uuid: String, callback: () -> Any?){
        firestoreProvider.listenForUser(uuid) {
            if(it != null) {
                _authenticatedUser.value = it
            } else {
                _authenticatedUser.value = null
            }
            callback()
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
    }
}