package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.databinding.ItemFinanceBinding
import com.lawlett.planner.ui.base.BaseAdapter

class FinanceAdapter :
    BaseAdapter<FinanceModel, ItemFinanceBinding>(
        R.layout.item_category,
        listOf(),
        ItemFinanceBinding::inflate
    ) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: ItemFinanceBinding, model: FinanceModel) {
        if (model.isIncome == true) {
            binding.amountTv.text = " + ${model.amount}"
            binding.description.text = model.description
            binding.dateTv.text = model.date
        } else {
            binding.amountTv.text = " - ${model.amount}"
            binding.description.text = model.description
            binding.dateTv.text = model.date
        }
        if (model.date=="") {
            binding.amountTv.text = binding.amountTv.context.getString(R.string.history_empty)
        }
    }
}