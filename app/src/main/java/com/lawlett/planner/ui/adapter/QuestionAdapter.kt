package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.QuestionModel
import com.lawlett.planner.databinding.ItemQuestionBinding
import com.lawlett.planner.ui.base.BaseAdapter

class QuestionAdapter : BaseAdapter<QuestionModel, ItemQuestionBinding>(
    R.layout.item_question,
    listOf(),
    ItemQuestionBinding::inflate
){
    override fun onBind(binding: ItemQuestionBinding, model: QuestionModel) {
        binding.questionTv.text= model.question
    }
}