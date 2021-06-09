package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.databinding.MainTaskItemBinding
import com.lawlett.planner.utils.BaseAdapter

class TaskProgressAdapter :BaseAdapter<TasksModel,MainTaskItemBinding>(R.layout.main_task_item,
    listOf(),inflater = MainTaskItemBinding::inflate) {
    override fun onBind(binding: MainTaskItemBinding, model: TasksModel) {
        binding.taskTitle.text=model.category
        binding.completeTaskCount.text= model.doneAmount.toString()
        binding.categoryImage.setImageResource(R.drawable.ic_work)
    }
}