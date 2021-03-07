package com.handily.model

/**
 * User class represents system users. It is constructed with builder
 * pattern to allow possibility of different optional parameters
 *
 * @author Maciej Furmańczyk
 * @version 1.0
 *
 * @param uuid - mandatory identifier of the user - uid from Firebase users
 * @param email - mandatory email address given during registration process
 * @param givenName - optional parameter for user's given name
 * @param familyName - optional parameter for user's family name
 * @param countryPhoneCode - optional parameter for country phone code. If given, [phoneNumber] is mandatory.
 * @param phoneNumber - optional parameter for user's phone number. Mandatory if [countryPhoneCode] is given.
 * @param pictureUrl - optional. Url link to user's profile picture]
 */

class User(
        val uuid: String? = null,
        val email: String? = null,
        val givenName: String? = null,
        val familyName: String? = null,
        val countryPhoneCode: String? = null,
        val phoneNumber: String? = null,
        val pictureUrl: String? = null
) {
        data class Builder(
                private val uuid: String,
                private val email: String,
                private var givenName: String? = null,
                private var familyName: String? = null,
                private var countryPhoneCode: String? = null,
                private var phoneNumber: String? = null,
                private var pictureUrl: String? = null
        ){
                fun givenName(givenName: String?) = apply { this.givenName = givenName }
                fun familyName(familyName: String?) = apply { this.familyName = familyName }
                fun countryPhoneCode(countryPhoneCode: String?) = apply { this.countryPhoneCode = countryPhoneCode }
                fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
                fun pictureUrl(pictureUrl: String?) = apply { this.pictureUrl = pictureUrl }
                fun build() = User(
                        uuid,
                        email,
                        givenName,
                        familyName,
                        countryPhoneCode,
                        phoneNumber,
                        pictureUrl)
        }
}