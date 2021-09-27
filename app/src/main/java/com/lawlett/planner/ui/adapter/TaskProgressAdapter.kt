package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.databinding.ItemMainTaskBinding
import com.lawlett.planner.ui.base.BaseAdapter

class TaskProgressAdapter : BaseAdapter<CategoryModel, ItemMainTaskBinding>(R.layout.item_main_task,
    listOf(),inflater = ItemMainTaskBinding::inflate) {
    @SuppressLint("SetTextI18n")
    override fun onBind(binding: ItemMainTaskBinding, model: CategoryModel) {
        binding.categoryTitle.text=model.categoryName
        binding.categoryCount.text=model.taskAmount.toString() + " / " +  model.doneTaskAmount.toString()
        binding.categoryImage.text = model.categoryIcon
        binding.categoryProgress.max = model.taskAmount!!
        binding.categoryProgress.progress = model.doneTaskAmount
    }
}