package com.lawlett.planner.ui.habit

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.databinding.FragmentHabitBinding
import com.lawlett.planner.ui.adapter.HabitAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.CreateHabitBottomSheetDialog
import org.koin.android.ext.android.inject
import java.util.*

class HabitFragment : BaseFragment<FragmentHabitBinding>(FragmentHabitBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<HabitModel> {
    private val viewModel by inject<HabitViewModel>()
    val adapter = HabitAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
    }

    private fun initClickers() {
        binding.addHabitFab.setOnClickListener { initBottomSheet() }
        backClick()
    }

    private fun initBottomSheet() {
        val bottomDialog = CreateHabitBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun initAdapter() {
        val adapter = HabitAdapter()
        binding.habitRecycler.adapter = adapter
        adapter.listener = this
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

    private fun checkDay(habitModel: HabitModel) {
        val calendar = Calendar.getInstance()
        val currentDay = calendar[Calendar.DAY_OF_MONTH]
        val dayFromRoom: Int = habitModel.myDay
        if (currentDay != dayFromRoom) {
            val today = (habitModel.currentDay + 1)
            val model = HabitModel(
                id = habitModel.id,
                myDay = currentDay,
                currentDay = today,
                title = habitModel.title,
                icon = habitModel.icon,
                allDays = habitModel.allDays
            )
            viewModel.update(model)
        } else {
            Toast.makeText(requireContext(), R.string.your_habit_is_done, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(model: HabitModel) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage(getString(R.string.you_is_done_habit_today))
            .setCancelable(false)
            .setPositiveButton("Конечно") { _, _ ->
                checkDay(model)
            }
            .setNegativeButton("Ещё нет") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.attention_alert))
        alert.show()
    }
}