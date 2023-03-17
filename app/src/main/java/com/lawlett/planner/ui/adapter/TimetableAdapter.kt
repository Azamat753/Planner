package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TimetableModel
import com.lawlett.planner.databinding.ItemTimetableBinding
import com.lawlett.planner.ui.base.BaseAdapter

class TimetableAdapter : BaseAdapter<TimetableModel, ItemTimetableBinding>(
    R.layout.item_timetable,
    listOf(),
    inflater = ItemTimetableBinding::inflate

) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: ItemTimetableBinding, model: TimetableModel) {
       binding.title.text = model.title
        binding.startTime.text = model.startTime +" - "
        binding.endTime.text = model.endTime
        binding.line.setBackgroundColor(model.color)
    }
}