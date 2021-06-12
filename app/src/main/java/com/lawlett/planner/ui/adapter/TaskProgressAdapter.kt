package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.databinding.ItemMainTaskBinding
import com.lawlett.planner.ui.base.BaseAdapter

class TaskProgressAdapter : BaseAdapter<TasksModel, ItemMainTaskBinding>(R.layout.item_main_task,
    listOf(),inflater = ItemMainTaskBinding::inflate) {
    override fun onBind(binding: ItemMainTaskBinding, model: TasksModel) {
        binding.taskTitle.text=model.category
        binding.completeTaskCount.text= model.doneAmount.toString()
        binding.categoryImage.setImageResource(R.drawable.ic_work)
    }
}