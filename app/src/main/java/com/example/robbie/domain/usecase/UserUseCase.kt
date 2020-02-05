package com.example.robbie.domain.usecase

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.robbie.data.model.Checkin
import com.example.robbie.data.model.User
import com.example.robbie.data.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

class UserUseCase {

    private val fireStoreRepository = FireStoreRepository()
    private val auth = FirebaseAuth.getInstance()

    fun findByUserId() : Flowable<User> {
        return Observable.create<User> { observer ->
            FireStoreRepository().getUsers().addSnapshotListener{ snapshot, exception ->
                if (exception != null) {
                    Log.d("Robbie FireStore Failed", exception.toString())
                    observer.onError(exception)
                    return@addSnapshotListener
                }
                if (snapshot == null || snapshot.isEmpty) {
                    observer.onNext(User(auth.currentUser!!.uid, ""))
                    return@addSnapshotListener
                }
                observer.onNext(snapshot.map {
                    it.toObject(User::class.java)
                }.filter {
                    it.userId == auth.currentUser!!.uid
                }.getOrElse(0) { User(auth.currentUser!!.uid, "") }
                )
            }
            return@create
        }.toFlowable(BackpressureStrategy.ERROR)
    }

    // Store EmployeeId
    fun storeEmployeeId(app: Application, user: User) {
        fireStoreRepository.saveUser(user).addOnCompleteListener {
            Toast.makeText(app, "更新しました", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(app, "更新失敗しました", Toast.LENGTH_LONG).show()
        }
    }
}