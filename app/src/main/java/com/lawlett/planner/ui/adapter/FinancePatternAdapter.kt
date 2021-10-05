package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.databinding.ItemFinanceBinding
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.utils.Constants

class FinancePatternAdapter :
    BaseAdapter<FinanceModel, ItemFinanceBinding>(
        R.layout.item_category,
        listOf(),
        ItemFinanceBinding::inflate
    ) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: ItemFinanceBinding, model: FinanceModel) {
            binding.amountTv.text = "${model.amount}"
            binding.description.text = model.description
            binding.dateTv.gone()
    }
}
