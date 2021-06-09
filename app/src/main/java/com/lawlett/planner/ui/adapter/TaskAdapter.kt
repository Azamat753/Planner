package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.databinding.TaskItemBinding
import com.lawlett.planner.utils.BaseAdapter

class TaskAdapter : BaseAdapter<TasksModel, TaskItemBinding>(
    R.layout.task_item,
    listOf(),
    inflater = TaskItemBinding::inflate
) {
    override fun onBind(binding: TaskItemBinding, model: TasksModel) {
        binding.taskCheckbox.text = model.task
        binding.taskCheckbox.isChecked = model.isDone
        listener?.onClick(model)
    }
}