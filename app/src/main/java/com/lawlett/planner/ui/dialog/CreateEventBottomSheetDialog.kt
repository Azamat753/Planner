package com.lawlett.planner.ui.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.databinding.CreateEventBottomSheetBinding
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.util.*

class CreateEventBottomSheetDialog :
    BaseBottomSheetDialog<CreateEventBottomSheetBinding>(CreateEventBottomSheetBinding::inflate) {
    private val viewModel by inject<EventViewModel>()
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        getBundleFromCalendar()
        fillDialog()
    }

    private fun fillDialog() {
        if (isUpdate()) {
            val model: EventModel = arguments?.getSerializable(Constants.UPDATE_MODEL) as EventModel
            binding.titleEditText.setText(model.title)
            binding.dateText.text = model.date
            binding.timeText.text = model.time
            binding.remindText.text = model.remindTime
        }
    }

    private fun isUpdate(): Boolean {
        return tag.equals(Constants.UPDATE_MODEL)
    }

    private fun getBundleFromCalendar() {
        if (arguments != null) {
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
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                binding.dateText.text = " ${theMonth(selectedMonth)}  $selectedDay  "
            },
            year, month, day
        )
        datePicker.show()
    }

    fun theMonth(month: Int): String {
        val monthNames = arrayOf(
            getString(R.string.january),
            getString(R.string.february),
            getString(R.string.march),
            getString(R.string.april),
            getString(R.string.may),
            getString(R.string.june),
            getString(R.string.july),
            getString(R.string.august),
            getString(R.string.september),
            getString(R.string.october),
            getString(R.string.november),
            getString(R.string.december)
        )
        return monthNames[month]
    }

    @SuppressLint("SetTextI18n")
    private fun pickTime(isRemind: Boolean) {
        var myHour = ""
        var myMinute = ""
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePicker =
            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->

                    myHour = if (selectedHour.toString().count() == 1) {
                        "0$selectedHour"
                    } else {
                        selectedHour.toString()
                    }

                    myMinute = if (selectedMinute.toString().count() == 1) {
                        "0$selectedMinute"
                    } else {
                        selectedMinute.toString()
                    }

                    if (isRemind) {
                        binding.remindText.text = "$myHour : $myMinute"
                    } else {
                        binding.timeText.text = "$myHour : $myMinute"
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
                if (isUpdate()) {
                    updateModel(title, date, time, remind, color)
                } else {
                    insertModel(title, date, time, remind, color)
                }
                findNavController().navigate(R.id.events_fragment)
                dismiss()
            }
        }
    }

    private fun insertModel(
        title: String,
        date: String,
        time: String,
        remind: String,
        color: Int
    ) {
        val model = EventModel(
            title = title,
            date = date,
            time = time,
            remindTime = remind,
            color = color
        )
        if (isUpdate()) {
            viewModel.update(model)
        } else {
            viewModel.addData(model)
        }
    }

    private fun updateModel(
        title: String,
        date: String,
        time: String,
        remind: String,
        color: Int
    ) {
        val model: EventModel = arguments?.getSerializable(Constants.UPDATE_MODEL) as EventModel
        val updateModel = EventModel(
            id = model.id,
            title = title,
            date = date,
            time = time,
            remindTime = remind,
            color = color
        )
        viewModel.update(updateModel)
    }

}