package com.lawlett.planner.ui.adapter

import android.graphics.Color
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.ThemeModel
import com.lawlett.planner.databinding.ItemThemeBinding
import com.lawlett.planner.ui.base.BaseAdapter

class ThemeAdapter : BaseAdapter<ThemeModel, ItemThemeBinding>(
    R.layout.item_theme,
    listOf(),
    inflater = ItemThemeBinding::inflate)
{
    override fun onBind(binding: ItemThemeBinding, model: ThemeModel) {
        binding.themeColorTv.text = model.colorText
        binding.themeColorTv.setBackgroundColor(Color.parseColor(model.colorAttr))
    }
}
