package com.handily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.handily.model.FixOffer
import com.handily.repository.FirestoreProvider


class OfferViewModel(application: Application): AndroidViewModel(application) {

    private val _fixOfferList = MutableLiveData<List<FixOffer>>()
    val fixOfferList: LiveData<List<FixOffer>>
        get() = _fixOfferList


    fun setFixOfferList(fixUuid: String) {
        FirestoreProvider.instance.getFixOffers(fixUuid) {
            _fixOfferList.value = it
        }
    }
}