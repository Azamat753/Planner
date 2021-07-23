package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.MainListModel
import com.lawlett.planner.databinding.ItemMainListBinding
import com.lawlett.planner.ui.base.BaseAdapter

class MainListAdapter : BaseAdapter<MainListModel, ItemMainListBinding>(
    R.layout.item_main_list,
    listOf(), ItemMainListBinding::inflate
) {
    override fun onBind(binding: ItemMainListBinding, model: MainListModel) {
        val habitAdapter = HabitAdapter()
        val eventAdapter = EventAdapter()
        val ideaAdapter= IdeaAdapter()
        val categoryAdapter= CategoryAdapter()
        binding.mainRecycler.adapter = habitAdapter
        binding.mainRecycler.adapter= eventAdapter
        binding.mainRecycler.adapter= ideaAdapter
        binding.mainRecycler.adapter= categoryAdapter
        model.listIdea?.let { ideaAdapter.setData(it) }
        model.listEvent?.let { eventAdapter.setData(it) }
        model.listHabit?.let { habitAdapter.setData(it) }
        model.listTask?.let { categoryAdapter.setData(it) }
    }
}