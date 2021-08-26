package com.lawlett.planner.ui.dialog.fragment

import android.os.Bundle
import android.view.View
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.ChooseTimeBottomSheetDialogBinding
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class ChooseTimeBottomSheetDialog :
    BaseBottomSheetDialog<ChooseTimeBottomSheetDialogBinding>(ChooseTimeBottomSheetDialogBinding::inflate) {
    val viewModel by inject<SkillViewModel>()
    val model: SkillModel? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValueForTimePicker()
        setTitle()
        initClickers()
        getSkillModel()
    }

    private fun setTitle() {
        binding.title.text = getSkillModel().skillName
    }

    fun getSkillModel(): SkillModel {
        return arguments?.getSerializable(Constants.SKILL_MODEL) as SkillModel
    }

    fun Double.roundTo(n: Int): Double {
        return "%.${n}f".format(this).toDouble()
    }

    private fun initClickers() {
        binding.applyButton.setOnClickListener {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val formatDateCreated = sdf.format(Date()).toString()
            val hour = binding.hourPicker.value * 60
            val minute = binding.minutePicker.value
            val calculatedHours: Double = ((hour + minute) / 60.0).roundTo(1)
            val totalHour =
                if (getSkillModel().hour.isNullOrEmpty()) "0" else (getSkillModel().hour?.toDouble()
                    ?.plus(calculatedHours)).toString()
            val dateCreated: String =
                if (getSkillModel().dateCreated.isNullOrEmpty()) formatDateCreated else getSkillModel().dateCreated.toString()
            var model = SkillModel(
                id = getSkillModel().id,
                hour = totalHour,
                skillName = getSkillModel().skillName,
                dateCreated = dateCreated
            )
            viewModel.update(model)
            dismiss()
        }
    }

    private fun setValueForTimePicker() {
        binding.hourPicker.minValue = 0
        binding.hourPicker.maxValue = 24
        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59
    }
}