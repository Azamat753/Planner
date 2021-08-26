package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.databinding.ItemTaskBinding
import com.lawlett.planner.ui.base.BaseAdapter

class TaskAdapter : BaseAdapter<TasksModel, ItemTaskBinding>(
    R.layout.item_task,
    listOf(),
    inflater = ItemTaskBinding::inflate
) {
    override fun onBind(binding: ItemTaskBinding, model: TasksModel) {
        binding.taskTv.text = model.task
        binding.taskCheck.isChecked = model.isDone
        binding.taskCheck.setOnClickListener { listener?.onClick(model) }
    }
}