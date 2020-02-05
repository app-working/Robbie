package com.example.robbie.data.repository

import android.util.Log
import com.example.robbie.data.model.Checkin
import com.example.robbie.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Flowable
import io.reactivex.Observable

class FireStoreRepository {

    var firestoreDB = FirebaseFirestore.getInstance()

    // save user to firebase[Users]
    fun saveUser(user: User): Task<Void> {
        var documentReference = firestoreDB.collection("users").document(user.userId)
        return documentReference.set(user)
    }

    // get user from firebase[Users]
    fun getUsers() : CollectionReference {
        var collectionReference = firestoreDB.collection("users")
        return collectionReference
    }

    // get user from firebase[Users]
    fun getCheckins(employeeId: String) : CollectionReference {
        var collectionReference = firestoreDB.collection("checkins/${employeeId}/eventid")
        return collectionReference
    }

    // save user to firebase[Checkins]
    fun saveCheckin(checkin: Checkin): Task<Void> {
        var documentReference =
            firestoreDB.collection("checkins").document(checkin.employeeId)
                .collection("eventid").document(checkin.eventId.toString())
        return documentReference.set(checkin)
    }

    // get user from firebase[Users]
    fun getUserPoints(employeeId: Int) : DocumentReference {
        var documentReference = firestoreDB.collection("checkins/${employeeId.toString()}/eventid").document()
        return documentReference
    }

    // singleton Factory
    companion object Factory {

        val instance: FireStoreRepository
            @Synchronized get() {
                return FireStoreRepository()
            }
    }
}