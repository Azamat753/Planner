package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.databinding.ItemEventBinding
import com.lawlett.planner.ui.base.BaseAdapter

class EventAdapter : BaseAdapter<EventModel, ItemEventBinding>(
    R.layout.item_event,
    listOf(),
    ItemEventBinding::inflate
) {
    override fun onBind(binding: ItemEventBinding, model: EventModel) {
        binding.title.text = model.title
        binding.date.text = model.date
        binding.time.text = model.time
        binding.line.setBackgroundColor(model.color)
    }
}