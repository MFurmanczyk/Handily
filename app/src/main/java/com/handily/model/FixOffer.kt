package com.handily.model

import com.google.firebase.firestore.DocumentId

class FixOffer (
    @DocumentId
    val uuid: String? = null,
    val workerUuid: String? = null,
    val fixUuid: String? = null,
    val price: Int? = null,
    val currency: String? = null) {

    data class Builder(
        private var workerUuid: String,
        private var fixUuid: String,
        private var price: Int,
        private var currency: String
    ){
        fun workerUuid(workerUuid: String) = apply { this.workerUuid = workerUuid }
        fun fixUuid(fixUuid: String) = apply { this.fixUuid }
        fun price(price: Int) = apply { this.price = price }
        fun currency(currency: String) = apply { this.currency = currency }
        fun build() = FixOffer(
            workerUuid = workerUuid,
            fixUuid = fixUuid,
            price = price,
            currency = currency
        )
    }
}