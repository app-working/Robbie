package com.example.robbie.ui.top

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.robbie.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.fragment_checkin.*
import java.lang.IllegalStateException

class CheckinDialogFragment(val dialogTitle: String, val dialogMessage: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog, null)
            view.apply {
                title.text = dialogTitle
                message.text = dialogMessage
            }
            builder.setView(view)
                .setPositiveButton("閉じる",
                    DialogInterface.OnClickListener { _, _ -> // 何もしない
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
