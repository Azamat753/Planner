package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.LanguageModel
import com.lawlett.planner.databinding.ItemLangaugeBinding
import com.lawlett.planner.ui.base.BaseAdapter

class LanguageAdapter : BaseAdapter<LanguageModel, ItemLangaugeBinding>(
    R.layout.item_theme,
    listOf(),
    inflater = ItemLangaugeBinding::inflate

) {

    override fun onBind(binding: ItemLangaugeBinding, model: LanguageModel) {
        binding.languageTv.text = model.language
    }
}