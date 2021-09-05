package com.lawlett.planner.ui.dialog.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.databinding.CreateEventBottomSheetBinding
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class CreateEventBottomSheetDialog :
    BaseBottomSheetDialog<CreateEventBottomSheetBinding>(CreateEventBottomSheetBinding::inflate) {
    private val viewModel by inject<EventViewModel>()
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        getBundleFromCalendar()
    }

    private fun getBundleFromCalendar() {
        if (arguments!=null){
            val date = arguments?.getString("date")
            binding.dateText.text = date
        }
    }

    private fun initClickers() {
        binding.applyButton.setOnClickListener { insertToDataBase() }
        binding.dateButton.setOnClickListener { pickDate() }
        binding.timeButton.setOnClickListener { pickTime(false) }
        binding.remindButton.setOnClickListener { pickTime(true) }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun pickDate() {
        val dateFormat = SimpleDateFormat("E, MMM d")
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, _, _, _ ->
                val date = dateFormat.format(calendar.time)
                binding.dateText.text = date.toString()
            },
            year, month, day
        )
        datePicker.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun pickTime(isRemind: Boolean) {
        val timeFormat = SimpleDateFormat("HH:MM")
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePicker =
            TimePickerDialog(
                requireContext(),
                { _, _, _ ->
                    val time = timeFormat.format(calendar.time)
                    if (isRemind) {
                        binding.remindText.text = time.toString()
                    } else {
                        binding.timeText.text = time.toString()
                    }
                },
                hour,
                minute,
                true
            )
        timePicker.show()
    }

    private fun insertToDataBase() {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val title = binding.titleEditText.text.toString()
        val date = binding.dateText.text.toString()
        val time = binding.timeText.text.toString()
        val remind = binding.remindText.text.toString()
        when {
            title.isEmpty() -> {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
            date.isEmpty() -> {
                binding.dateButton.error = getString(R.string.fill_field)
            }
            time.isEmpty() -> {
                binding.timeButton.error = getString(R.string.fill_field)
            }
            else -> {
                val model = EventModel(
                    title = title,
                    date = date,
                    time = time,
                    remindTime = remind,
                    color = color
                )
                viewModel.addData(model)
                dismiss()
            }
        }
    }

}