package com.lawlett.planner.ui.dialog

import android.os.Bundle
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.CreateSkillBottomSheetDialogBinding
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import org.koin.android.ext.android.inject

class CreateSkillBottomSheetDialog(val checkListEvent: CheckListEvent) :
    BaseBottomSheetDialog<CreateSkillBottomSheetDialogBinding>(CreateSkillBottomSheetDialogBinding::inflate){
    val viewModel by inject<SkillViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
    }

    private fun initClickers() {
        binding.applyButton.setOnClickListener {
            val skillName = binding.titleEditText.text.toString().trim().trim()
            val skill = SkillModel(skillName = skillName)
            if (skillName.isNotEmpty()) {
                viewModel.insertData(skill)
                checkListEvent.check()
                dismiss()
            } else {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
        }
    }
}
