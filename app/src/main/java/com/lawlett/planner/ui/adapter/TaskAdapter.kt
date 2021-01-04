package com.lawlett.planner.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.Tasks
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter(private val listener: Listener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var taskList = emptyList<Tasks>()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(task: Tasks, listener: Listener) {
            itemView.task_ch.text = task.task
            itemView.task_ch.isChecked = task.isDone
            itemView.task_ch.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(taskList[position], listener)
    }

    fun setData(task: List<Tasks>) {
        this.taskList = task
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemClick(pos: Int)
    }
}