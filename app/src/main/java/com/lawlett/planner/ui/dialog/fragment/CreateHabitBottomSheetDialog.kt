package com.lawlett.planner.ui.dialog.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.models.IconModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.databinding.CreateHabitBottomSheetDialogBinding
import com.lawlett.planner.extensions.getIcons
import com.lawlett.planner.extensions.showToast
import com.lawlett.planner.ui.adapter.IconAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import org.koin.android.ext.android.inject

class CreateHabitBottomSheetDialog :
    BaseBottomSheetDialog<CreateHabitBottomSheetDialogBinding>(CreateHabitBottomSheetDialogBinding::inflate), BaseAdapter.IBaseAdapterClickListener<IconModel> {
    var isImageChoose: Boolean = false
    var icon: String = ""
    val viewModel by inject<HabitViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setValueForDayPicker()
    }

    private fun setValueForDayPicker() {
        binding.dayPicker.minValue=7
        binding.dayPicker.maxValue=365
    }

    private fun initListeners() {
        binding.iconButton.setOnClickListener { iconPickerDialog() }
        binding.applyBtn.setOnClickListener { insertHabit() }
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
                    allDays =allDays,
                )
                dismiss()
                viewModel.insertData(habit)
            }
        }
    }

    private fun iconPickerDialog() {
        val adapter = IconAdapter()
        adapter.listener = this
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_icon)
        val recyclerView = dialog.findViewById(R.id.icon_recycler) as RecyclerView
        recyclerView.adapter = adapter
        adapter.setData(getIcons())
        if (isImageChoose) {
            dialog.cancel()
        }
        dialog.show()
    }

    override fun onClick(model: IconModel) {
        icon = model.icon
        isImageChoose = true
        binding.iconTv.text = model.icon
    }
}