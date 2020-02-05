package com.example.robbie.domain.usecase

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.example.robbie.data.model.Checkin
import com.example.robbie.data.repository.FireStoreRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.zip.DeflaterOutputStream

class CheckinUseCase {

    private val fireStoreRepository = FireStoreRepository()

    // fetchCheckinInfo
    fun findByEmployeeId(employeeId: String) : Flowable<List<Checkin>> {
        return Observable.create<List<Checkin>> { observer ->
            FireStoreRepository().getCheckins(employeeId).addSnapshotListener{ snapshot, exception ->
                if (exception != null) {
                    Log.d("FireStore Failed", exception.toString())
                    observer.onError(exception)
                    return@addSnapshotListener
                }
                if (snapshot == null || snapshot.isEmpty) {
                    observer.onNext(listOf())
                    return@addSnapshotListener
                }
                observer.onNext(snapshot.map {
                    it.toObject(Checkin::class.java)
                }.sortedByDescending {
                    it.eventId
                })
            }
            return@create
        }.toFlowable(BackpressureStrategy.ERROR)
    }

    // Store CheckinInfo
    fun storeCheckinInfo(app: Application, checkinInfo: Checkin) {
        fireStoreRepository.saveCheckin(checkinInfo).addOnCompleteListener {
            Toast.makeText(app, "更新しました", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(app, "更新が失敗しました", Toast.LENGTH_LONG).show()
        }
    }



}