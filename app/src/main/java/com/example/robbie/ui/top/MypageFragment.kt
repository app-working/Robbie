package com.example.robbie.ui.top

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.robbie.R
import com.example.robbie.data.repository.FirebaseAuthRepository
import com.example.robbie.databinding.FragmentMypageBinding
import kotlinx.android.synthetic.main.fragment_mypage.*

class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    lateinit var prefs : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        prefs = this.activity!!.getSharedPreferences("Robbie", AppCompatActivity.MODE_PRIVATE)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        binding.setLifecycleOwner(this);
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = this.activity!!.getSharedPreferences("Robbie", AppCompatActivity.MODE_PRIVATE)
        val employeeId = prefs.getString("EmployeeId", "0") ?: "0"
        Log.d("Robbie pref employee id is ", employeeId)
        val factory =  MypageViewModel.Factory(this, FirebaseAuthRepository().getCurrentUser()!!, employeeId)
        val viewModel = ViewModelProviders.of(this, factory).get(MypageViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.employeeInfo.observe(this, Observer {
            if (!it.employeeId.isNullOrEmpty()) {
                prefs.edit().putString("EmployeeId", it.employeeId).apply()
            }
        })
        viewModel.checkinHistorys.observe(this, Observer {
            it?.let {
                viewModel.setUserPoint(it)
            }
        })
    }
}