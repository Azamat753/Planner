package com.lawlett.planner.ui.dialog.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.models.IconModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.databinding.CreateHabitBottomSheetDialogBinding
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.getIcons
import com.lawlett.planner.extensions.showToast
import com.lawlett.planner.service.MessageService
import com.lawlett.planner.ui.adapter.IconAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.Constants.TITLE
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class CreateHabitBottomSheetDialog :
    BaseBottomSheetDialog<CreateHabitBottomSheetDialogBinding>(CreateHabitBottomSheetDialogBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<IconModel> {
    private var isImageChoose: Boolean = false
    var icon: String = ""
    val viewModel by inject<HabitViewModel>()
    private val calendar = Calendar.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setValueForDayPicker()
        setDataForUpdate()
    }

    private fun setValueForDayPicker() {
        binding.dayPicker.minValue = 7
        binding.dayPicker.maxValue = 365
    }

    fun isUpdate(): Boolean {
        return tag.toString() == Constants.UPDATE_MODEL
    }

    private fun setDataForUpdate() {
        if (isUpdate()) {
            val model: HabitModel = arguments?.getSerializable(Constants.HABIT_MODEL) as HabitModel
            binding.titleEditText.setText(model.title)
            binding.remindText.text = model.remindTime
            binding.iconTv.text = model.icon
            binding.dayPicker.value = model.allDays.toInt()
        }
    }




    private fun initListeners() {
        binding.iconButton.setOnClickListener { iconPickerDialog() }
        binding.applyBtn.setOnClickListener {
            if (isUpdate()) {
                updateHabit()
            } else {
                insertHabit()
            }
        }
    }

    private fun insertHabit() {
        when {
            icon == "" -> {
                binding.iconButton.error = getString(R.string.choose_image)
            }
            binding.titleEditText.text.toString().isEmpty() -> {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
            else -> {
                val title = binding.titleEditText.text.toString()
                val allDays = binding.dayPicker.value.toString()
                val habit = HabitModel(
                    title = title,
                    icon = icon,
                    allDays = allDays,
                )
                viewModel.insertData(habit)
                dismiss()
            }
        }
    }

    private fun updateHabit() {
        val model: HabitModel = arguments?.getSerializable(Constants.HABIT_MODEL) as HabitModel
        if (binding.titleEditText.text.toString().isEmpty()) {
            binding.titleEditText.error = getString(R.string.fill_field)
        } else {
            val title = binding.titleEditText.text.toString()
            val allDays = binding.dayPicker.value.toString()
            val currentIcon = binding.iconTv.text.toString()
            val habit = HabitModel(
                title = title,
                icon = currentIcon,
                allDays = allDays,
                id = model.id,
                currentDay = model.currentDay,
                myDay = model.myDay
            )
            viewModel.update(habit)
            dismiss()
        }
    }



    private fun iconPickerDialog() {
        val adapter = IconAdapter()
        adapter.listener = this
        val dialog = requireContext().getDialog(R.layout.dialog_icon)
        val titleCard: View = dialog.findViewById(R.id.title_card)
        val title = titleCard.findViewById<TextView>(R.id.title)
        title.text = "Выбор иконки"
        val recyclerView = dialog.findViewById(R.id.icon_recycler) as RecyclerView
        recyclerView.adapter = adapter
        adapter.setData(getIcons())
        if (isImageChoose) {
            dialog.cancel()
        }
        dialog.show()
    }

    override fun onClick(model: IconModel,position:Int) {
        icon = model.icon
        isImageChoose = true
        binding.iconTv.text = model.icon
    }
}