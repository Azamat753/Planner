package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.databinding.FocusItemBinding
import com.lawlett.planner.extensions.toDecimal
import com.lawlett.planner.ui.base.BaseAdapter

class FocusAdapter : BaseAdapter<SkillModel, FocusItemBinding>(
    R.layout.focus_item,
    listOf(),
    FocusItemBinding::inflate
) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: FocusItemBinding, model: SkillModel) {
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