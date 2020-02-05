package com.example.robbie.ui.top

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.robbie.data.model.Checkin
import com.example.robbie.databinding.HistoryItemBinding

class HistoryAdapter(private val lifeCycleOwner: LifecycleOwner, private val viewModel: HistoryViewModel): RecyclerView.Adapter<HistoryAdapter.BindingHolder>() {

    init {
        viewModel.checkinHistorys.observe({ lifeCycleOwner.lifecycle }, {it?.apply {
            update(this)
        }})
    }

    private var items: MutableList<Checkin> = arrayListOf()

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.BindingHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.eventName.text = items[position].eventName
        holder.checkinTime.text = items[position].registerTime.toString()
        holder.point.text = items[position].eventPoint.toString()+ "pt"
        holder.binding.viewModel = viewModel
        holder.binding.lifecycleOwner = lifeCycleOwner
    }

    class BindingHolder(val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val eventName: TextView = binding.textEventName
        val checkinTime: TextView = binding.textCheckinTime
        val point: TextView = binding.textPoint
    }

    private fun update(list: List<Checkin>) {
        val adapter = recyclerView.adapter as HistoryAdapter
        val diff = DiffUtil.calculateDiff(DiffCallback(adapter.items, list))
        diff.dispatchUpdatesTo(adapter)
        this.items.clear()
        this.items.addAll(list)
    }

    class DiffCallback(private val oldList: List<Checkin>, private val newList: List<Checkin>): DiffUtil.Callback() {

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = oldList[oldPosition] == (newList[newPosition])

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = oldList[oldPosition].eventId == (newList[newPosition]).eventId

        override fun getNewListSize() = newList.size

        override fun getOldListSize() = oldList.size
    }
}