package com.lawlett.planner.ui.adapter

import android.graphics.Color
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.ThemeModel
import com.lawlett.planner.databinding.ThemeItemBinding
import com.lawlett.planner.utils.BaseAdapter

class ThemeAdapter() : BaseAdapter<ThemeModel, ThemeItemBinding>(
    R.layout.theme_item,
    listOf(),
    inflater = ThemeItemBinding::inflate)
{
    override fun onBind(binding: ThemeItemBinding, model: ThemeModel) {
        binding.themeColorTv.text = model.colorText
        binding.themeColorTv.setBackgroundColor(Color.parseColor(model.colorAttr))
    }
}
