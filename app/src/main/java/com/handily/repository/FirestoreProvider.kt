package com.handily.repository

import android.graphics.Bitmap
import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.handily.model.FixRequest
import com.handily.model.FixRequestContract
import com.handily.model.User
import com.handily.model.UsersContract
import java.io.ByteArrayOutputStream


private const val TAG = "FirestoreProvider"

class FirestoreProvider private constructor(){

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()

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

    fun getFixRequests(userUuid: String, callback: (List<FixRequest>?) -> Unit) {
        db.collection(FixRequestContract.COLLECTION_NAME)
            .whereEqualTo(FixRequestContract.Fields.USER_UUID, userUuid)
            .get()
            .addOnSuccessListener { documents ->
                val fixRequests : ArrayList<FixRequest> = ArrayList()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    fixRequests.add(document.toObject())
                }
                callback(fixRequests)
            }
            .addOnFailureListener {
                Log.w(TAG, "Listen failed.", it.cause)
                callback(null)
            }
    }

    fun getFixRequests(location: LatLng, radius: Int, callback: (List<FixRequest>?) -> Unit) {
        val center = GeoLocation(location.latitude, location.longitude)
        val radiusInM = (radius * 1000).toDouble()
        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds){
            val q = db.collection(FixRequestContract.COLLECTION_NAME)
                .orderBy("geoHash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                val fixRequests: ArrayList<FixRequest> = ArrayList()
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap.documents) {
                        val lat = doc.getDouble("latitude")!!
                        val lng = doc.getDouble("longitude")!!

                        val docLocation = GeoLocation(lat, lng)
                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                        if (distanceInM <= radiusInM) {
                            fixRequests.add(doc.toObject()!!)
                        }
                    }
                }
                callback(fixRequests)
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

    fun addFixRequest(fixRequest: FixRequest, bitmaps: ArrayList<Bitmap>?, callback: () -> Unit) {
        db.collection(FixRequestContract.COLLECTION_NAME).add(fixRequest).addOnCompleteListener {
            if(bitmaps != null) {
                if(bitmaps.isNotEmpty()) {
                    val bitmapsToUpload = ArrayList(bitmaps)
                    addPhotos(bitmapsToUpload, it.result.id)
                    callback()
                }
            }
        }

    }

    private fun updateFixRequest(fixRequestUuid: String?, imageUrls: ArrayList<String>) {
        db.collection(FixRequestContract.COLLECTION_NAME).document(fixRequestUuid!!).get().addOnCompleteListener {
            if(it.isSuccessful) {
                db.collection(FixRequestContract.COLLECTION_NAME)
                    .document(fixRequestUuid)
                    .update(FixRequestContract.Fields.PICTURES, imageUrls)
            }
        }
    }

    fun addPhotos(bitmaps: ArrayList<Bitmap>, fixRequestUuid: String?) {
        val imageUrls = arrayListOf<String>()
        for(i in 0 until bitmaps.size) {
            val storageRef = storage.reference
            val fixImageRef = storageRef.child("${fixRequestUuid}_$i.jpg")
            val baos = ByteArrayOutputStream()
            bitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = fixImageRef.putBytes(data)
            uploadTask.addOnCompleteListener {
                if(it.isSuccessful) {
                    val imageUrl = fixImageRef.downloadUrl
                    imageUrl.addOnCompleteListener { task ->
                        imageUrls.add(task.result.toString())
                        if(imageUrls.size == bitmaps.size) {
                            updateFixRequest(fixRequestUuid, imageUrls)
                        }
                    }
                }
            }
        }

    }

    //Singleton pattern handling
    private object SingletonHolder {
        val instance = FirestoreProvider()
    }

    companion object {
        val instance: FirestoreProvider by lazy { SingletonHolder.instance }
    }
}