package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.databinding.ItemHabitBinding
import com.lawlett.planner.ui.base.BaseAdapter

class HabitAdapter : BaseAdapter<HabitModel, ItemHabitBinding>(
    R.layout.item_habit,
    listOf(),
    ItemHabitBinding::inflate
) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: ItemHabitBinding, model: HabitModel) {
        binding.habitTitle.text = model.title
        binding.habitImage.setImageResource(R.drawable.ic_done)
        binding.habitCount.text = model.currentDay + " / " + model.allDays
        binding.habitProgress.max = model.allDays.toInt()
        binding.habitProgress.progress = model.currentDay.toInt()
    }
}