package com.lawlett.planner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.databinding.ItemTaskBinding

class SimpleTaskAdapter(private val checkListener: CheckListener?) :
    RecyclerView.Adapter<SimpleTaskAdapter.SimpleTaskViewHolder>() {
    private var list: List<TasksModel> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleTaskViewHolder {
        val itemBinding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpleTaskViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SimpleTaskViewHolder, position: Int) {
        val model: TasksModel = list[position]
        holder.bind(model, checkListener!!)
    }

    fun update(list: List<TasksModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class SimpleTaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TasksModel, listener: CheckListener) {
            binding.taskTv.text = model.task
            binding.taskCheck.isChecked = model.isDone
            itemView.setOnLongClickListener {
                listener.longClick(model,itemView)
                return@setOnLongClickListener true
            }
            binding.taskCheck.setOnClickListener {
                listener.checkClick(model, adapterPosition)
            }
        }
    }
}