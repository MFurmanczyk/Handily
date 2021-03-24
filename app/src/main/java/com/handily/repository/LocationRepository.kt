package com.handily.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.SingleEmitter


class LocationRepository(val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun getUserLocation(): Single<LatLng> {
        return Single.create { emitter: SingleEmitter<LatLng> ->
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val location = LatLng(it.result.latitude, it.result.longitude)
                        emitter.onSuccess(location)
                    } else {
                        if(it.result == null) {
                            emitter.onError(UnsupportedOperationException("Location settings turned off."))
                        }
                        it.exception?.let { it1 -> emitter.onError(it1) }
                    }
                }
            } else {
                emitter.onError(SecurityException("No permission granted."))
            }
        }
    }
}