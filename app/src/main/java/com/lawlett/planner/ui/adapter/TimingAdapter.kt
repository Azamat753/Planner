package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.databinding.TimingItemBinding
import com.lawlett.planner.extensions.toDecimal
import com.lawlett.planner.ui.base.BaseAdapter
import java.text.DecimalFormat

class TimingAdapter : BaseAdapter<SkillModel, TimingItemBinding>(
    R.layout.timing_item,
    listOf(),
    TimingItemBinding::inflate
) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: TimingItemBinding, model: SkillModel) {
        val hour = if (model.hour.isNullOrEmpty()) "0" else model.hour
        if (hour.toDouble() < 1) {
            val minute = hour.toDouble() * 60
            binding.hourAmount.text = "${minute.toDecimal()} Минут"
        } else {
            val decimalHour = model.hour?.toDouble()?.toDecimal()
            binding.hourAmount.text = "$decimalHour Часов"
        }
        binding.timerTaskTitle.text = model.skillName
        binding.createdDate.text = model.dateCreated
    }
}