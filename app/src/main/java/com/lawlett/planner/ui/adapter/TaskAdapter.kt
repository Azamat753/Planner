package com.lawlett.planner.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TasksModel
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter(private val listener: Listener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    lateinit var view: View
    private var taskList = emptyList<TasksModel>()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var taskViewHolder: TaskViewHolder

        @SuppressLint("ClickableViewAccessibility")
        fun onBind(task: TasksModel, listener: Listener) {
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(taskList[position], listener)

    }
    fun setData(task: List<TasksModel>) {
        this.taskList = task
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemClick(pos: Int)
    }
}