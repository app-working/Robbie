package com.example.robbie.ui.top

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.robbie.R
import com.example.robbie.databinding.FragmentCheckinBinding
import com.google.zxing.integration.android.IntentIntegrator


class CheckinFragment : Fragment() {

    private lateinit var binding: FragmentCheckinBinding
    private lateinit var viewModel: CheckinViewModel
    lateinit var prefs : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        prefs = this.activity!!.getSharedPreferences("Robbie", AppCompatActivity.MODE_PRIVATE)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkin, container, false)
        binding.setLifecycleOwner(this);
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory =  CheckinViewModel.Factory(requireActivity() as AppCompatActivity, this)
        viewModel = ViewModelProviders.of(this, factory).get(CheckinViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.employeeInfo.observe(this, Observer {
            Log.d("Robbie viewmodel.employeeInfo observe ", "employeeId is " + it.employeeId)
            if (!it.employeeId.isNullOrEmpty()) {
                prefs.edit().putString("EmployeeId", it.employeeId).apply()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scanData = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        scanData.contents?.let {
            viewModel.storeCheckinInfo(it)
        }
    }
}