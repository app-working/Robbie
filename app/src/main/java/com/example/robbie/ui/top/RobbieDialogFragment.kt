package com.example.robbie.ui.top

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.robbie.R
import com.example.robbie.data.model.Checkin
import com.example.robbie.util.isLuckey
import kotlinx.android.synthetic.main.dialog.view.*
import java.lang.IllegalStateException

class CheckinDialogFragment(val checkinInfo: Checkin) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog, null)
            view.apply {
                title.text = "Robbie"
                message.text = getDialogMessage(checkinInfo)
            }
            // 当選の場合は、ダイアログ(View)の内容を変更する
            if (isLuckey(checkinInfo)) {
                view.title.setBackgroundColor(Color.RED)
                view.title.text = "ア　タ　リ︎︎"
                view.title.setTextColor(Color.WHITE)
                view.image_checkin.setImageResource(R.drawable.baseline_redeem_white_24)
                view.image_checkin.setColorFilter(Color.RED)
            }
            builder.setView(view)
                .setPositiveButton("閉じる",
                    DialogInterface.OnClickListener { _, _ -> // 何もしない
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun getDialogMessage(checkinInfo: Checkin) : String {
        return "チェックインが完了しました" + "\n" + checkinInfo.eventName + "\n" + "（社員番号 : " + checkinInfo.employeeId + "）"
    }
}
