package com.example.robbie.ui.top

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.robbie.R
import com.example.robbie.data.model.Checkin
import com.example.robbie.data.repository.FirebaseAuthRepository
import com.example.robbie.domain.usecase.CheckinUseCase
import com.example.robbie.domain.usecase.UserUseCase
import com.example.robbie.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class CheckinViewModel(private val fragment: Fragment) : AndroidViewModel(fragment.activity!!.application) {

    val schedulerProvider = AppSchedulerProvider()
    var employeeInfo = UserUseCase().findByUserId().subscribeOn(schedulerProvider.io()).toLiveData()

    // Checkin Event
    fun doCheckin(view: View) {
        if (employeeInfo.value == null || employeeInfo.value!!.employeeId == "") {
            AlertDialog.Builder(fragment.context!!)
                .setTitle("Confirm")
                .setMessage("社員番号が登録されていません。社員番号を更新しますか？")
                .setPositiveButton("はい") {_, _ ->
                    val bottomNavigationView = fragment.activity!!.findViewById<BottomNavigationView>(R.id.navigation)
                    bottomNavigationView.menu.getItem(2).setChecked(true)
                    fragment.fragmentManager!!.beginTransaction().replace(R.id.nav_host_fragment, MypageFragment()).commit()
                }
                .setNegativeButton("キャンセル") {_, _ -> }
                .show()
        } else {
            IntentIntegrator.forSupportFragment(fragment).apply {
                setOrientationLocked(true)
                setPrompt("QRコードを読み込んでください")
                captureActivity = RobbieCaptureActivity::class.java
            }.initiateScan()
        }
    }

    // Store CheckinInfo
    fun storeCheckinInfo(qrData: String) {
        // イベントデータ(JSON)読み込み
            // ex.{"event_id":2, "event_name":"2020年5月WM&P部門会議", "event_point":1, "remarks":"通常開催", "lottery_rate":10}
        try {
            // QRコード(JSON)パース
            val eventInfo = JSONObject(qrData)
            Log.d("Robbie EventInfo is", eventInfo.toString())
            val checkinInfo = Checkin()
            checkinInfo.apply {
                eventId = eventInfo.getInt("event_id")
                eventName = eventInfo.getString("event_name")
                eventPoint = eventInfo.getInt("event_point")
                remarks = eventInfo.getString("remarks")
                employeeId = employeeInfo.value!!.employeeId
                employeeName = FirebaseAuthRepository().getCurrentUser()!!.displayName!!
                // 抽選
                status = doLottery(eventInfo.getInt("lottery_rate"))
            }

            // チェックイン情報更新(新規登録 or 上書き)
            CheckinUseCase().storeCheckinInfo(checkinInfo, fragment)
        } catch (e: Exception) {
            Log.d("Robbie QRRead Failed", e.toString())
            AlertDialog.Builder(fragment.context!!)
                .setIcon(R.drawable.robbie)
                .setTitle("エラー")
                .setMessage("QRコードの読み込みに失敗しました。")
                .setPositiveButton("はい") {_, _ ->
                }
                .show()
        }
    }

    // ViewModel Factory
    class Factory(private val activity: AppCompatActivity, private val fragment: Fragment) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CheckinViewModel(fragment) as T
        }
    }
}