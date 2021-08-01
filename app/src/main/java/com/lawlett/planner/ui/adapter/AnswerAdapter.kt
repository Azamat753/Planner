package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.AnswerModel
import com.lawlett.planner.databinding.ItemAnswerBinding
import com.lawlett.planner.ui.base.BaseAdapter

class AnswerAdapter : BaseAdapter<AnswerModel, ItemAnswerBinding>(
    R.layout.item_answer, listOf(), ItemAnswerBinding::inflate
) {
    override fun onBind(binding: ItemAnswerBinding, model: AnswerModel) {
        binding.answerTv.text=model.answer
    }
}