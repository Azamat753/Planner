package com.lawlett.planner.ui.dialog

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.databinding.CreateEventBottomSheetBinding
import com.lawlett.planner.extensions.theMonth
import com.lawlett.planner.service.MessageService
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.util.*

class CreateEventBottomSheetDialog(var checkList: CheckListEvent?) :
    BaseBottomSheetDialog<CreateEventBottomSheetBinding>(CreateEventBottomSheetBinding::inflate) {
    private val viewModel by inject<EventViewModel>()
    private val calendar = Calendar.getInstance()
    var choosedHour: Int = 0
    var choosedMinute: Int = 0
    var choosedYear: Int = 0
    var choosedMonth: Int = 0
    var choosedDay: Int = 0
    var isClickRemind=false
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
        binding.remindButton.setOnClickListener {
            if (requestPermission()) {
                pickTime(true)
            }
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun pickDate() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                choosedYear = selectedYear
                choosedMonth = selectedMonth
                choosedDay = selectedDay
                binding.dateText.text =
                    " ${theMonth(selectedMonth, requireContext())}  $selectedDay  "
            },
            year, month, day
        )
        datePicker.show()
    }

    private fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivityForResult(intent, 1)
                return false
            }
        }
        return true
    }

    private fun setNotification(
        hour: Int,
        minute: Int,
        title: String,
        year: Int,
        month: Int,
        day: Int
    ) {
        val i = Intent(requireContext(), MessageService::class.java)
        i.putExtra("displayText", "sample text")
        i.putExtra(Constants.TITLE, "Planner+")
        i.putExtra(Constants.TEXT, title)
        val pi = PendingIntent.getBroadcast(
            requireContext(),
            minute,
            i,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, minute)
        val time = calendar.timeInMillis
        val mAlarm = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarm[AlarmManager.RTC_WAKEUP, time] = pi
    }

    @SuppressLint("SetTextI18n")
    private fun pickTime(isRemind: Boolean) {
        var myHour: String
        var myMinute: String
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePicker =
            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    choosedHour = selectedHour
                    choosedMinute = selectedMinute
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
                        isClickRemind=true
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
        val title = binding.titleEditText.text.toString().trim()
        val date = binding.dateText.text.toString().trim()
        val time = binding.timeText.text.toString().trim()
        val remind = binding.remindText.text.toString().trim()
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
                    checkList?.check()
                    insertModel(title, date, time, remind, color)
                }

                if (isClickRemind){
                    setNotification(choosedHour, choosedHour, title,choosedYear,choosedMonth,choosedDay)
                }
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