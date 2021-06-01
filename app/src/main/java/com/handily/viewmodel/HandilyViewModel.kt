package com.handily.viewmodel


import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.handily.model.FixRequest
import com.handily.model.User
import com.handily.repository.FirestoreProvider
import com.handily.repository.LocationRepository
import com.handily.util.Cities
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class  HandilyViewModel(application: Application): AndroidViewModel(application) {

    private val _authenticatedUser = MutableLiveData<User>()
    val authenticatedUser: LiveData<User>
        get() = _authenticatedUser

    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng>
        get() = _userLocation

    private val _ownedFixRequests = MutableLiveData<List<FixRequest>>()
    val ownedFixRequest: LiveData<List<FixRequest>>
        get() = _ownedFixRequests

    private val _fixRequestPhotos = MutableLiveData<ArrayList<Bitmap>>()
    val fixRequestPhotos: LiveData<ArrayList<Bitmap>>
        get() = _fixRequestPhotos

    private val locationRepository = LocationRepository(application.applicationContext)

    private val disposable = CompositeDisposable()

    fun setUser(uuid: String, callback: () -> Any?){
        FirestoreProvider.instance.listenForUser(uuid) {
            if(it != null) {
                _authenticatedUser.value = it
            } else {
                _authenticatedUser.value = null
            }
            callback()
        }
    }

    fun getOwnedFixRequests(userUuid: String) {
        FirestoreProvider.instance.getFixRequests(userUuid) {
            _ownedFixRequests.postValue(it)
        }
    }

    fun getFixRequests(location: LatLng) {

    }

    fun getFixRequest(uuid: String): FixRequest? {
        return null
    }

    fun postFixRequest(fixRequest: FixRequest) {
        FirestoreProvider.instance.addFixRequest(fixRequest, fixRequestPhotos.value) {
            _fixRequestPhotos.value!!.clear()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getFixPhotos(data: Intent?) {
        // if multiple images are selected
        val bitmaps = ArrayList<Bitmap>()
        val clipData = data?.clipData
        val singleData = data?.data
        if (clipData != null) {
            val count = clipData.itemCount
            for (i in 0 until count) {
                val imageUri: Uri = clipData.getItemAt(i).uri
                val source = ImageDecoder.createSource(getApplication<Application>().contentResolver, imageUri)
                bitmaps.add(ImageDecoder.decodeBitmap(source))
            }
        } else if (singleData != null) {
            // if single image is selected
            val imageUri: Uri = singleData
            val source = ImageDecoder.createSource(getApplication<Application>().contentResolver, imageUri)
            bitmaps.add(ImageDecoder.decodeBitmap(source))
        }
        _fixRequestPhotos.value = bitmaps
    }


    fun getUserLocation() {
        disposable.add(
            locationRepository.getUserLocation()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<LatLng>() {
                    override fun onSuccess(t: LatLng) {
                        setLocation(t)
                    }

                    override fun onError(e: Throwable) {
                        setLocation(Cities.WARSAW.location)
                    }

                })
        )

    }



    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun setLocation(location: LatLng) {
        _userLocation.value = location
    }

    fun signOut() {
        Firebase.auth.signOut()
    }
}