package com.example.robbie.ui.top

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.robbie.data.model.Checkin
import com.example.robbie.data.model.User
import com.example.robbie.domain.usecase.CheckinUseCase
import com.example.robbie.domain.usecase.UserUseCase
import com.example.robbie.util.AppSchedulerProvider
import com.example.robbie.util.toLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import kotlinx.android.synthetic.main.fragment_mypage.*

class MypageViewModel(
    private val fragment: Fragment, private val currentUser: FirebaseUser, val employeeId: String
    ) : AndroidViewModel(fragment.activity!!.application) {

    val schedulerProvider = AppSchedulerProvider()

    var firebaseuser: MutableLiveData<FirebaseUser> = MutableLiveData()
    var provider: MutableLiveData<UserInfo> = MutableLiveData()
    var employeeInfo = UserUseCase().findByUserId().subscribeOn(schedulerProvider.io()).toLiveData()
    var checkinHistorys = CheckinUseCase().findByEmployeeId(employeeId).subscribeOn(schedulerProvider.io()).toLiveData()
    var userPoint = MutableLiveData<String>()

    init {
        firebaseuser.postValue(currentUser)
        // providerData(List<UserInfo>)の2番目の要素に、アカウントのプロバイダーデータが格納される（1番目は"firebase"が格納
        provider.postValue(currentUser.providerData[1])
    }

    // get UzerPoint
    fun setUserPoint(checkinHistory: List<Checkin>) {
        userPoint.postValue(checkinHistory.sumBy { it.eventPoint }.toString() + " pt")
    }

    // store EmployeeId
    fun storeEmployeeId(view: View) {
        val regex = """\d{7}""".toRegex()
        if (!regex.matches(employeeInfo.value!!.employeeId)) {
            fragment.edit_employeeId.setError("不正な値が入力されました")
        } else {
            val user = User(employeeInfo.value!!.userId, employeeInfo.value!!.employeeId)
            UserUseCase().storeEmployeeId(fragment.activity!!.application, user)
        }
    }

    // ViewModel Factory
    class Factory(private val fragment: Fragment, private val user: FirebaseUser, private val employeeId: String) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MypageViewModel(fragment, user, employeeId) as T
        }
    }
}