package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.databinding.TimingItemBinding
import com.lawlett.planner.ui.base.BaseAdapter

class TimingAdapter : BaseAdapter<SkillModel, TimingItemBinding>(
    R.layout.timing_item,
    listOf(),
     TimingItemBinding::inflate
) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: TimingItemBinding, model: SkillModel) {
        val hour = if(model.hour.isNullOrEmpty()) "0" else model.hour
        binding.timerTaskTitle.text = model.skillName
        binding.hourAmount.text = "$hour Часов"
        binding.createdDate.text = model.dateCreated
    }
}