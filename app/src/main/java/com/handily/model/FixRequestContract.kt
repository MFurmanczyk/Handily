package com.handily.model

object FixRequestContract {

    internal const val COLLECTION_NAME = "FixRequests"

    object Fields {
        const val UUID = "uuid"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val LAT = "latitude"
        const val LNG = "longitude"
        const val PICTURES = "imagesUrls"
        const val USER_UUID = "userUuid"
    }
}