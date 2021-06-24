package com.handily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handily.model.FixRequest
import com.handily.model.User
import com.handily.repository.FirestoreProvider

class FixOverviewViewModel(application: Application): AndroidViewModel(application) {

    private val _fixRequest = MutableLiveData<FixRequest>()
    val fixRequest: LiveData<FixRequest>
        get() = _fixRequest

    fun setFixRequest(fixUuid: String) {
        FirestoreProvider.instance.getFixRequest(fixUuid) {
            _fixRequest.value = it
        }
    }
}