package com.example.robbie.ui.top

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.robbie.data.model.Checkin
import com.example.robbie.domain.usecase.CheckinUseCase
import com.example.robbie.domain.usecase.UserUseCase
import com.example.robbie.util.AppSchedulerProvider
import com.example.robbie.util.toLiveData

class HistoryViewModel(private val fragment: Fragment, employeeId: String) : AndroidViewModel(fragment.activity!!.application) {

    val schedulerProvider = AppSchedulerProvider()
    var checkinHistorys = CheckinUseCase().findByEmployeeId(employeeId).subscribeOn(schedulerProvider.io()).toLiveData()

    // historyList Display

    // ViewModel Factory
    class Factory(private val fragment: Fragment, private val employeeId: String) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HistoryViewModel(fragment, employeeId) as T
        }
    }
}

