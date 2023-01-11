package com.lawlett.planner.ui.dialog

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.databinding.tool.ext.T
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import com.google.android.material.timepicker.MaterialTimePicker
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.DreamModel
import com.lawlett.planner.data.room.viewmodels.DreamViewModel
import com.lawlett.planner.databinding.CreateDreamBottomSheetBinding
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.time.Duration
import java.time.LocalTime
import java.util.*
import kotlin.concurrent.fixedRateTimer


class CreateDreamBottomSheetDialog(val checkListEvent: CheckListEvent) :
    BaseBottomSheetDialog<CreateDreamBottomSheetBinding>(CreateDreamBottomSheetBinding::inflate) {
    private val viewModel by inject<DreamViewModel>()
    private val calendar = Calendar.getInstance()
    var startHour: Int = 0
    var startMinute: Int = 0
    var endHour: Int = 0
    var endMinute: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setDataForUpdate()
        initListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListener() {
        binding.applyButton.setOnClickListener { insertDataToDataBase() }
        binding.startTimeButton.setOnClickListener { pickTime(true) }
        binding.endTimeButton.setOnClickListener { pickTime(false) }
    }


    private fun setTitle() {
        binding.titleCard.title.text = getString(R.string.sleep_recording)
    }

    private fun isUpdate(): Boolean {
        return arguments != null
    }

    private fun setDataForUpdate() {
        if (isUpdate()) {
            val model: DreamModel = arguments?.getSerializable(Constants.UPDATE_MODEL) as DreamModel
            startHour = model.wokeUp.substringBefore(":").trim().toInt()
            startMinute = model.wokeUp.substringAfter(":").trim().toInt()
            endHour = model.fellAsleep.substringBefore(":").trim().toInt()
            endMinute = model.fellAsleep.substringAfter(":").trim().toInt()

            binding.titleEditText.setText(model.dream)
            binding.startTimeText.text = model.wokeUp
            binding.endTimeText.text = model.fellAsleep
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertDataToDataBase() {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val title = binding.titleEditText.text.toString().trim()
        val startTime = binding.startTimeText.text.toString().trim()
        val endTime = binding.endTimeText.text.toString().trim()
        when {
            startTime.isEmpty() -> {
                binding.startTimeText.error = getString(R.string.fill_field)
            }
            endTime.isEmpty() -> {
                binding.endTimeText.error = getString(R.string.fill_field)
            }
            else -> {
                if (isUpdate()) {
                    updateModel(startTime, endTime, title)
                } else {
                    insertModel(
                        startTime,
                        endTime,
                        color,
                        title,
                        getTodayDate(requireContext()),
                        getRemainingTime()
                    )
                    checkListEvent.check()
                }
                dismiss()
            }
        }
    }

    private fun insertModel(
        startTime: String,
        endTime: String,
        color: Int,
        dream: String,
        dateCreated: String,
        sleptHour: String
    ) {
        val model = DreamModel(
            wokeUp = startTime,
            fellAsleep = endTime,
            color = color,
            dream = dream,
            dateCreated = dateCreated,
            sleptHour = sleptHour
        )
        viewModel.insertData(model)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateModel(startTime: String, endTime: String, title: String) {
        val model: DreamModel = arguments?.getSerializable(Constants.UPDATE_MODEL) as DreamModel
        val updateModel = DreamModel(
            id = model.id,
            wokeUp = startTime,
            fellAsleep = endTime,
            color = model.color,
            dream = title,
            sleptHour = getRemainingTime(),
            dateCreated = model.dateCreated
        )
        viewModel.update(updateModel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRemainingTime(): String {
        val res = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)
        return LocalTime.MIN.plus(Duration.ofMinutes(res.toLong())).toString()
    }


    @SuppressLint("SetTextI18n")
    private fun pickTime(isStart: Boolean) {
        var myHour = ""
        var myMinute = ""
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val myTimeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            if (view.isShown ) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
            }
        }
        val timePicker = TimePickerDialog(
            requireContext(),R.style.TimePickerSpinner,{ _, selectedHour, selectedMinute ->
              if (isStart) {
                    startHour = selectedHour
                    startMinute = selectedMinute

                } else {
                    endHour = selectedHour
                    endMinute = selectedMinute
                }
                //                    if (startHour==0 && startMinute==0 ){
                //                todo
                //                    }
                myHour = if (selectedHour.toString()
                        .count() == 1
                ) { // сетит текст для времени
                    "0$selectedHour"
                } else {
                    selectedHour.toString()
                }

                myMinute = if (selectedMinute.toString().count() == 1) {
                    "0$selectedMinute"
                } else {
                    selectedMinute.toString()
                }

                if (isStart) {
                    binding.startTimeText.text = "$myHour : $myMinute"  //сохранение времени

                } else {
                    binding.endTimeText.text = "$myHour : $myMinute"

                }
         }, hour, minute, true
        )

        timePicker.show()


    }
}