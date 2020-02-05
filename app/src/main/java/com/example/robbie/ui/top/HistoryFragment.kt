package com.example.robbie.ui.top

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.robbie.R
import com.example.robbie.databinding.FragmentHistoryBinding
import com.example.robbie.domain.usecase.UserUseCase
import com.example.robbie.util.AppSchedulerProvider
import com.example.robbie.util.toLiveData
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.setLifecycleOwner(this);
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = this.activity!!.getSharedPreferences("Robbie", AppCompatActivity.MODE_PRIVATE)
        val employeeId = prefs.getString("EmployeeId", "0") ?: "0"
        val factory =  HistoryViewModel.Factory(this, employeeId)
        viewModel = ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.checkinHistorys.observe(this, Observer {
            if (it.isNullOrEmpty()) binding.isLoading = true
        })
        val historyAdapter = HistoryAdapter(this, viewModel)
        list_checkin.layoutManager = LinearLayoutManager(this.activity)
        list_checkin.adapter = historyAdapter

    }
}