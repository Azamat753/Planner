package com.lawlett.planner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.data.room.models.MainListModel
import com.lawlett.planner.databinding.ItemMainListBinding

class MainAdapter(private val list: List<MainListModel>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    class MainViewHolder(private val binding: ItemMainListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(model: MainListModel) {
            val eventAdapter = EventAdapter()
            val ideaAdapter=IdeaAdapter()
            val taskAdapter=TaskAdapter()
            val habitAdapter=HabitAdapter()
            model.listEvent?.let { eventAdapter.setData(it) }
            when (adapterPosition) {
                0 -> binding.mainRecycler.adapter = habitAdapter
                1-> binding.mainRecycler.adapter= taskAdapter
                2-> binding.mainRecycler.adapter=ideaAdapter
                3-> binding.mainRecycler.adapter=eventAdapter
            }
        }
    }
 
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemMainListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return 4
    }
}