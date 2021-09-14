package com.lawlett.planner.ui.dialog.fragment

import android.os.Bundle
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.data.room.viewmodels.FinanceViewModel
import com.lawlett.planner.databinding.FinanceBottomSheetDialogBinding
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class FinanceBottomSheetDialog :
    BaseBottomSheetDialog<FinanceBottomSheetDialogBinding>
        (FinanceBottomSheetDialogBinding::inflate) {
    val viewModel by inject<FinanceViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(tag.toString())
        showAddPatternDialog()
        workWithPattern()
        initClickers()
    }

    private fun showAddPatternDialog() {
        if (tag == Constants.PATTERN_CATEGORY) {
            binding.title.text = "Добавить шаблон"
            binding.descriptionWrapper.hint = getString(R.string.enter_name)
            binding.amountWrapper.gone()
        }
    }

    private fun workWithPattern() {
        if (tag == Constants.WORK_WITH_PATTERN) {
            val argModel: FinanceModel =
                arguments?.getSerializable(Constants.WORK_WITH_PATTERN) as FinanceModel
            binding.title.text = argModel.description
            binding.descriptionWrapper.gone()
        }
    }

    private fun getModel(): FinanceModel {
        return arguments?.getSerializable(Constants.WORK_WITH_PATTERN) as FinanceModel
    }

    private fun addUpdateModel(
        argModel: FinanceModel,
    ) {
        val updateAmount = binding.amountEditText.text.toString().toInt() + getModel().amount
        val model = FinanceModel(
            id = getModel().id,
            description = argModel.description,
            date = argModel.description,
            amount = updateAmount,
            category = Constants.PATTERN_CATEGORY
        )
        viewModel.update(model)
        dismiss()
    }

    private fun addPattern() {
        val description = binding.descriptionEditText.text.toString()
        val model = FinanceModel(
            description = description,
            category = Constants.PATTERN_CATEGORY,
            amount = 0,
        )
        viewModel.addModel(model)
        dismiss()
    }

    private fun setTitle(title: String) {
        if (title == Constants.HISTORY_CATEGORY) {
            binding.title.text = getString(R.string.income)
        } else {
            binding.title.text = "Расход"
        }
    }

    private fun initClickers() {
        binding.applyButton.setOnClickListener {
            when (tag) {
                Constants.PATTERN_CATEGORY -> {
                    addPattern()
                }
                Constants.HISTORY_CATEGORY -> {
                    addModel()
                }
                Constants.WORK_WITH_PATTERN -> {
                    addUpdateModel(getModel())
                }
            }
        }
    }

    private fun addModel() {
        val amount = binding.amountEditText.text.toString().toInt()
        val description = binding.descriptionEditText.text.toString()
        val isIncome = arguments?.getBoolean(Constants.IS_INCOME)
        val model =
            FinanceModel(
                category = tag.toString(),
                amount = amount,
                description = description,
                date = getTodayDate(),
                isIncome = isIncome
            )
        viewModel.addModel(model)
        dismiss()
    }
}
