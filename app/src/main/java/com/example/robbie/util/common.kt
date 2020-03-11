package com.example.robbie.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.example.robbie.data.model.Checkin
import org.reactivestreams.Publisher
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random

class Constants {
    companion object {
        const val LOTTERY_NORMAL = 0
        const val LOTTERY_LUCKEY = 1
    }
}

fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this) as LiveData<T>

interface SchedulerProvider {
    fun ui(): Scheduler

    fun computation(): Scheduler

    fun newThread(): Scheduler

    fun io(): Scheduler
}

class AppSchedulerProvider : SchedulerProvider {
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun newThread(): Scheduler = Schedulers.newThread()

    override fun io(): Scheduler = Schedulers.io()
}

object CustomBindingAdapter {
    //xmlに定義する際のBindingAdapter

    @BindingAdapter("visibleGone")
    @JvmStatic
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}

fun isActiveNetwork(activity: Activity) : Boolean {
    val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    activeNetwork?.let {
        return it.isConnected
    }
    return false
}

fun doLottery(rate: Int) : Int {
    // 抽選処理
    if (rate > 0 && rate -1 == Random.nextInt(rate)) {
            return Constants.LOTTERY_LUCKEY
    }
    return Constants.LOTTERY_NORMAL
}

//  抽選結果取得(本来はModel/Entityに紐づく)
fun isLuckey(checkin: Checkin) : Boolean {
    if (checkin.status == Constants.LOTTERY_LUCKEY) return true
    return false
}