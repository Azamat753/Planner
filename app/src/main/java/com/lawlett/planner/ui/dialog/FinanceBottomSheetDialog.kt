package com.lawlett.planner.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.data.room.viewmodels.FinanceViewModel
import com.lawlett.planner.databinding.FinanceBottomSheetDialogBinding
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.IntPreference
import org.koin.android.ext.android.inject

class FinanceBottomSheetDialog(
    private val updateBalance: UpdateBalance,
    var checkListEvent: CheckListEvent
) :
    BaseBottomSheetDialog<FinanceBottomSheetDialogBinding>
        (FinanceBottomSheetDialogBinding::inflate) {
    val viewModel by inject<FinanceViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        showAddPatternDialog()
        workWithPattern()
        initClickers()
    }

    private fun showAddPatternDialog() {
        if (tag == Constants.PATTERN_CATEGORY) {
            binding.title.text = getString(R.string.add_pattern)
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
        when {
            binding.amountEditText.text.toString().isEmpty() -> {
                binding.descriptionEditText.error = getString(R.string.fill_field)
            }
            binding.amountEditText.text.toString().isEmpty() -> {
                binding.amountEditText.error = getString(R.string.fill_field)
            }
            else -> {
                val updateAmount = binding.amountEditText.text.toString().trim().toInt() + getModel().amount
                val amount = binding.amountEditText.text.toString().trim().toInt()
                countExpensiveAndIncome(false, amount)
                val model = FinanceModel(
                    id = getModel().id,
                    description = argModel.description,
                    date = argModel.description,
                    amount = updateAmount,
                    category = Constants.PATTERN_CATEGORY
                )
                viewModel.update(model)
                findNavController().navigate(R.id.financeFragment)
                dismiss()
            }
        }
    }

    private fun addPattern() {
        val description = binding.descriptionEditText.text.toString().trim()
        if (description.isEmpty()) {
            binding.descriptionEditText.error = getString(R.string.fill_field)
        } else {
            val model = FinanceModel(
                description = description,
                category = Constants.PATTERN_CATEGORY,
                amount = 0,
            )
            viewModel.addModel(model)
            checkListEvent.check()
            dismiss()
        }
    }

    private fun setTitle() {
        if (arguments?.getBoolean(Constants.IS_INCOME) == true) {
            binding.title.text = getString(R.string.income)
        } else {
            binding.title.text = getString(R.string.expensive)
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
        when {
            binding.descriptionEditText.text.toString().isEmpty() -> {
                binding.descriptionEditText.error = getString(R.string.fill_field)
            }
            binding.amountEditText.text.toString().isEmpty() -> {
                binding.amountEditText.error = getString(R.string.fill_field)
            }
            else -> {
                val amount = binding.amountEditText.text.toString().trim().toInt()
                val description = binding.descriptionEditText.text.toString().trim()
                val isIncome = arguments?.getBoolean(Constants.IS_INCOME)
                countExpensiveAndIncome(isIncome, amount)
                val model =
                    FinanceModel(
                        category = tag.toString(),
                        amount = amount,
                        description = description,
                        date = getTodayDate(requireContext()),
                        isIncome = isIncome
                    )
                viewModel.addModel(model)
                findNavController().navigate(R.id.financeFragment)
                dismiss()
            }
        }
    }

    private fun countExpensiveAndIncome(isIncome: Boolean?, amount: Int) {
        val previousIncome =
            IntPreference.getInstance(requireContext())?.getInt(Constants.INCOME) ?: 0
        val previousExpensive =
            IntPreference.getInstance(requireContext())?.getInt(Constants.EXPENSIVE) ?: 0

        if (isIncome == true) {
            IntPreference.getInstance(requireContext())
                ?.saveInt(Constants.INCOME, amount + previousIncome)
        } else {
            IntPreference.getInstance(requireContext())
                ?.saveInt(Constants.EXPENSIVE, amount + previousExpensive)
        }
        updateBalance.needUpdate()
    }

    interface UpdateBalance {
        fun needUpdate()
    }
}
