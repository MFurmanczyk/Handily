package com.handily.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

class FixRequest(
    @DocumentId
    val uuid: String? = null,
    @get:PropertyName("latitude")
    val lat: Double? = null,
    @get:PropertyName("longitude")
    val lng: Double? = null,
    val geoHash: String? = null,
    val name: String? = null,
    val description: String? = null,
    val imagesUrls: ArrayList<String>? = null,
    val userUuid: String? = null) {

        data class Builder(
            private val name: String,
            private val description: String,
            private var lat: Double? = null,
            private var lng: Double? = null,
            private var geoHash: String? = null,
            private var imagesUrls: ArrayList<String> = arrayListOf(),
            private var userUuid: String? = null
        ) {
            fun lat(lat: Double) = apply { this.lat = lat }
            fun lng(lng: Double) = apply { this.lng = lng }
            fun geoHash(hash: String) = apply { this.geoHash = hash }
            fun imagesUrls(imagesUrls: List<String>) = apply { this.imagesUrls.addAll(imagesUrls) }
            fun userUuid(uuid: String?) = apply { this.userUuid = uuid }
            fun build() = FixRequest(
                name = name,
                description = description,
                lat = lat,
                lng = lng,
                geoHash = geoHash,
                imagesUrls = imagesUrls,
                userUuid = userUuid
            )
        }
}
