package com.example.robbie.domain.usecase

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.robbie.data.model.Checkin
import com.example.robbie.data.repository.FireStoreRepository
import com.example.robbie.ui.top.CheckinDialogFragment
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

class CheckinUseCase {

    private val fireStoreRepository = FireStoreRepository()

    // CheckinInfo一覧取得
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

    // CheckinInfo一件取得
    fun findOne(employeeId: String, eventId: Int) : Flowable<List<Checkin>> {
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
                }.filter {
                    it.eventId == eventId
                })
            }
            return@create
        }.toFlowable(BackpressureStrategy.ERROR)
    }

    // Store CheckinInfo
    fun storeCheckinInfo(checkinInfo: Checkin, fragment: Fragment) : Boolean{
        return fireStoreRepository.saveCheckin(checkinInfo).addOnCompleteListener {
            CheckinDialogFragment(checkinInfo).show(fragment.activity!!.supportFragmentManager, "Robbie")
        }.addOnFailureListener {
            Log.d("Robbie storeCheckinInfo is ", "Not Successful")
        }.isSuccessful
    }

}