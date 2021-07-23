package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IconModel
import com.lawlett.planner.databinding.ItemIconBinding
import com.lawlett.planner.ui.base.BaseAdapter

class IconAdapter : BaseAdapter<IconModel, ItemIconBinding>(
    R.layout.item_icon,
    listOf(),
    ItemIconBinding::inflate
) {
    override fun onBind(binding: ItemIconBinding, model: IconModel) {
        binding.iconView.setImageResource(model.icon)
    }
}