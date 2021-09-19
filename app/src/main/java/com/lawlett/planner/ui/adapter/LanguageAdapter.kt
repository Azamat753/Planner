package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.LanguageModel
import com.lawlett.planner.databinding.ItemThemeBinding
import com.lawlett.planner.ui.base.BaseAdapter

class LanguageAdapter : BaseAdapter<LanguageModel, ItemThemeBinding>(
    R.layout.item_theme,
    listOf(),
    inflater = ItemThemeBinding::inflate

) {

    override fun onBind(binding: ItemThemeBinding, model: LanguageModel) {
        binding.themeColorTv.text = model.language
    }
}