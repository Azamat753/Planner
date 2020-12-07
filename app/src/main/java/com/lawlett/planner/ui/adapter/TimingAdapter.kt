package com.lawlett.planner.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.Timing
import kotlinx.android.synthetic.main.fragment_progress.view.*
import kotlinx.android.synthetic.main.timing_item.view.*

class TimingAdapter :RecyclerView.Adapter<TimingAdapter.TimingViewHolder>() {
    private var timingList= emptyList<Timing>()

    class TimingViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimingViewHolder {
        return TimingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.timing_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TimingViewHolder, position: Int) {
        val currentItem=timingList[position]
        holder.itemView.timer_task_title.text=currentItem.timerTitle
        holder.itemView.minutes_amount.text= currentItem.timerMinutes.toString()
        holder.itemView.task_day.text=currentItem.timerDay

    }
    override fun getItemCount(): Int {
        return timingList.size
    }
    fun setData(timing:List<Timing>){
        this.timingList=timing
        notifyDataSetChanged()
    }
}