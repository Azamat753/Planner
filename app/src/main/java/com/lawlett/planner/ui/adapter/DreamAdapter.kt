package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.DreamModel
import com.lawlett.planner.databinding.ItemDreamBinding
import com.lawlett.planner.ui.base.BaseAdapter

class DreamAdapter : BaseAdapter<DreamModel, ItemDreamBinding>(
    R.layout.item_dream,
    listOf(),
    ItemDreamBinding::inflate
) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: ItemDreamBinding, model: DreamModel) {
        binding.startTime.text = "C " + model.wokeUp
        binding.endTime.text = "До " + model.fellAsleep
        binding.sleptHour.text = "Ушло на сон "+model.sleptHour
        binding.dateCreated.text = model.dateCreated
        binding.line.setBackgroundColor(model.color)
    }
}