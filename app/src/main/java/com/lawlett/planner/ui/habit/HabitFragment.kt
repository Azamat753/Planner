package com.lawlett.planner.ui.habit

import android.os.Bundle
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.databinding.FragmentHabitBinding
import com.lawlett.planner.ui.adapter.HabitAdapter
import com.lawlett.planner.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class HabitFragment : BaseFragment<FragmentHabitBinding>(FragmentHabitBinding::inflate) {
    private val viewModel by inject<HabitViewModel>()
    val adapter = HabitAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        insertDataToDataBase("Прыгать", R.drawable.ic_person,"14","3","2")
    }

    private fun initAdapter() {
        val adapter = HabitAdapter()
        binding.habitRecycler.adapter = adapter
        getDataFromDataBase(adapter)
    }

    private fun getDataFromDataBase(adapter: HabitAdapter) {
        viewModel.getHabitsLiveData()
            .observe(viewLifecycleOwner, { ideas ->
                if (ideas.isNotEmpty()) {
                    adapter.setData(ideas)
                }
            })
    }

    private fun insertDataToDataBase(
        title: String,
        image: Int,
        allDays: String,
        currentDay: String,
        myDay: String
    ) {
        val habit = HabitModel(
            title = title,
            image = image,
            allDays = allDays,
            currentDay = currentDay,
            myDay = myDay
        )
        viewModel.addIdea(habit)
        adapter.notifyDataSetChanged()
    }
}