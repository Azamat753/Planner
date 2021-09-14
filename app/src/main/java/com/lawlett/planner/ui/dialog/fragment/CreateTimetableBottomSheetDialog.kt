package com.lawlett.planner.ui.dialog.fragment

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TimetableModel
import com.lawlett.planner.data.room.viewmodels.TimetableViewModel
import com.lawlett.planner.databinding.CreateTimetableBottomSheetBinding
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.util.*

class CreateTimetableBottomSheetDialog :
    BaseBottomSheetDialog<CreateTimetableBottomSheetBinding>(CreateTimetableBottomSheetBinding::inflate),
    AdapterView.OnItemSelectedListener {
    private val viewModel by inject<TimetableViewModel>()
    private val calendar = Calendar.getInstance()
    lateinit var dayOfWeek: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initSpinner()
        fillSpinner()
    }

    private fun fillSpinner() {
        if (tag.toString().isNotEmpty()) {
            binding.dayOfWeekSpinner.setSelection(tag.toString().toInt())
            setUpdateModel()
        }
    }

    private fun initListener() {
        binding.startTimeButton.setOnClickListener { pickTime(true) }
        binding.endTimeButton.setOnClickListener { pickTime(false) }
        binding.applyButton.setOnClickListener { insertDataToDataBase() }
    }

    private fun insertDataToDataBase() {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val title = binding.titleEditText.text.toString()
        val startTime = binding.startTimeText.text.toString()
        val endTime = binding.endTimeText.text.toString()
        when {
            title.isEmpty() -> {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
            startTime.isEmpty() -> {
                binding.startTimeText.error = getString(R.string.fill_field)
            }
            endTime.isEmpty() -> {
                binding.endTimeText.error = getString(R.string.fill_field)
            }
            else -> {
                if (isUpdate()){
                    updateModel(startTime, endTime, title)
                }else{
                    insertModel(startTime, endTime, color, title)
                }
                dismiss()
            }
        }
    }

    private fun insertModel(
        startTime: String,
        endTime: String,
        color: Int,
        title: String
    ) {
        val model = TimetableModel(
            dayOfWeek = dayOfWeek,
            startTime = startTime,
            endTime = endTime,
            color = color,
            title = title
        )
        viewModel.addData(model)
    }

    private fun updateModel(startTime: String, endTime: String, title: String) {
        val model: TimetableModel =
            arguments?.getSerializable(Constants.UPDATE_MODEL) as TimetableModel
        val updateModel = TimetableModel(
            id = model.id,
            startTime = startTime,
            endTime = endTime,
            color = model.color,
            title = title,
            dayOfWeek = dayOfWeek
        )
        viewModel.update(updateModel)
    }

    @SuppressLint("SetTextI18n")
    private fun pickTime(isStart: Boolean) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePicker =
            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    if (isStart) {
                        binding.startTimeText.text = "$selectedHour : $selectedMinute"
                    } else {
                        binding.endTimeText.text = "$selectedHour : $selectedMinute"
                    }
                },
                hour,
                minute,
                true
            )
        timePicker.show()
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dayOfWeek_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dayOfWeekSpinner.adapter = adapter
            binding.dayOfWeekSpinner.onItemSelectedListener = this
        }
    }

    private fun setUpdateModel() {
        if (isUpdate()) {
            val model: TimetableModel =
                arguments?.getSerializable(Constants.UPDATE_MODEL) as TimetableModel
            binding.startTimeText.text = model.startTime
            binding.endTimeText.text = model.endTime
            binding.titleCard.title.text = model.title
            binding.titleEditText.setText(model.title)
        }
    }

    private fun isUpdate(): Boolean {
        return arguments != null
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val arrayDays = resources.getStringArray(R.array.dayOfWeek_array)
        dayOfWeek = arrayDays[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}