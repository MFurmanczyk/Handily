package com.handily.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.handily.model.FixRequest
import com.handily.model.FixRequestContract
import com.handily.model.User
import com.handily.model.UsersContract
import kotlin.system.exitProcess

private const val TAG = "FirestoreProvider"

class FirestoreProvider private constructor(){

    private val db = Firebase.firestore

    /**
     * Updates user with given uuid
     * @param user user to be added to the "Users" collection with given [User.uuid]
     */
    fun addUser(user: User) {
        db.collection(UsersContract.COLLECTION_NAME).document(user.uuid!!).set(user)
    }

    /**
     * @param uuid - identifier of the user that is to be returned.
     * @return [User] with given [uuid]. Returns [null] if user does not exist.
     */

    fun getUser(uuid: String, callback: (User?) -> Unit) {

        db.collection(UsersContract.COLLECTION_NAME).document(uuid).get().addOnCompleteListener {
            var user: User? = null
            if(it.isSuccessful) {
                user = it.result?.toObject<User>()
            } else {
                Log.w(TAG, "getUser() failed.", it.exception?.cause)
            }
            callback(user)

        }
    }

    /**
     * @param uuid - identifier of the user that is to be returned.
     * @return [User] with given [uuid]. Returns {@code null} if user does not exist.
     */

    fun listenForUser(uuid: String, callback: (User?) -> Unit) {
        db.collection(UsersContract.COLLECTION_NAME)
            .document(uuid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    callback(null)
                    return@addSnapshotListener
                } else {
                    callback(value?.toObject<User>())
                }
            }
    }



    /**
    * Updates user with given uuid
     * @param updatedUser existing user with given [User.uuid]
     */
    fun updateUser(updatedUser: User) {
        db.collection(UsersContract.COLLECTION_NAME).document(updatedUser.uuid!!).get().addOnCompleteListener {
            if(it.isSuccessful) {
                db.collection(UsersContract.COLLECTION_NAME)
                    .document(updatedUser.uuid)
                    .set(updatedUser, SetOptions.merge())
            } else {
                Log.d(TAG, "Object not found", it.exception)
            }
        }
    }

    fun addFixRequest(fixRequest: FixRequest) {
        db.collection(FixRequestContract.COLLECTION_NAME).add(fixRequest)
    }

    //Singleton pattern handling
    private object SingletonHolder {
        val instance = FirestoreProvider()
    }

    companion object {
        val instance: FirestoreProvider by lazy { SingletonHolder.instance }
    }
}