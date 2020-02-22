package com.example.robbie.ui.top

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.robbie.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.fragment_checkin.*
import java.lang.IllegalStateException

class CheckinDialogFragment() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog, null))
                .setPositiveButton("閉じる",
                    DialogInterface.OnClickListener { _, _ -> // 何もしない
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
