package com.lawlett.planner.ui.dialog

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListTimeTable
import com.lawlett.planner.data.room.models.TimetableModel
import com.lawlett.planner.data.room.viewmodels.TimetableViewModel
import com.lawlett.planner.databinding.CreateTimetableBottomSheetBinding
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class CreateTimetableBottomSheetDialog(
    private val updateRecycler: UpdateRecycler?,
    private val checkList: CheckListTimeTable?
) :
    BaseBottomSheetDialog<CreateTimetableBottomSheetBinding>(CreateTimetableBottomSheetBinding::inflate),
    AdapterView.OnItemSelectedListener {
    private val viewModel by inject<TimetableViewModel>()
    private val calendar = Calendar.getInstance()
    lateinit var dayOfWeek: String
    private var listDayOfWeek = ArrayList<String>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        initListener()
        initSpinner()
        fillSpinner()
    }

    private fun setTitle() {
        binding.titleCard.title.text = getString(R.string.to_the_schedule)
        if (isUpdate()) {
            binding.applyButton.text = getString(R.string.apply)
        } else {
            binding.applyButton.text = getString(R.string.create)
        }
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
        binding.mondayBtn.setOnClickListener { addDayToList(binding.mondayBtn,0) }
        binding.tuesdayBtn.setOnClickListener { addDayToList(binding.tuesdayBtn,1) }
        binding.wednesdayBtn.setOnClickListener { addDayToList(binding.wednesdayBtn,2) }
        binding.thursdayBtn.setOnClickListener { addDayToList(binding.thursdayBtn,3) }
        binding.fridayBtn.setOnClickListener { addDayToList(binding.fridayBtn,4) }
        binding.saturdayBtn.setOnClickListener { addDayToList(binding.saturdayBtn,5) }
        binding.sundayBtn.setOnClickListener { addDayToList(binding.sundayBtn,6) }
    }

    private fun addDayToList(checkBox: CheckBox,day: Int) {
        val arrayDays = resources.getStringArray(R.array.dayOfWeek_keys)

        if (checkBox.isChecked) {
            listDayOfWeek.add(arrayDays[day])
        } else {
            listDayOfWeek.remove(arrayDays[day])
        }
    }

    private fun insertDataToDataBase() {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val title = binding.titleEditText.text.toString().trim()
        val startTime = binding.startTimeText.text.toString().trim()
        val endTime = binding.endTimeText.text.toString().trim()
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
                if (isUpdate()) {
                    updateModel(startTime, endTime, title)
                } else {
//                    insertModel(startTime, endTime, color, title,dayOfWeek)
                    for (i in 0 until listDayOfWeek.size) {
                        insertModel(startTime, endTime, color, title, listDayOfWeek[i])
                    }
                }
                updateRecycler?.needUpdate(binding.dayOfWeekSpinner.selectedItemPosition)
                checkList?.check(binding.dayOfWeekSpinner.selectedItemPosition)
                dismiss()
            }
        }
    }

    private fun insertModel(
        startTime: String,
        endTime: String,
        color: Int,
        title: String, choosedDayOfWeek: String
    ) {
        val model = TimetableModel(
            dayOfWeek = choosedDayOfWeek,
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
        var myHour: String
        var myMinute: String
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

                    if (isStart) {
                        binding.startTimeText.text = "$myHour : $myMinute"
                    } else {
                        binding.endTimeText.text = "$myHour : $myMinute"
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
        val arrayDays = resources.getStringArray(R.array.dayOfWeek_keys)
        dayOfWeek = arrayDays[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    interface UpdateRecycler {
        fun needUpdate(dayOfWeek: Int)
    }
}