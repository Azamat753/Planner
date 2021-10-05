package com.lawlett.planner.ui.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.databinding.CreatePasswordDialogBinding
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.StringPreference
import org.koin.android.ext.android.inject

class SetPasswordBottomSheetDialog :
    BaseBottomSheetDialog<CreatePasswordDialogBinding>(CreatePasswordDialogBinding::inflate) {
    private val viewModel by inject<CategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPassword()
    }

    private fun passwordInstall() {
        val model = arguments?.getSerializable(Constants.UPDATE_MODEL) as CategoryModel
        val updateModel = CategoryModel(
            id = model.id,
            categoryIcon = model.categoryIcon,
            categoryName = model.categoryName,
            taskAmount = model.taskAmount,
            isBlock = true
        )
        viewModel.update(updateModel)
    }

    private fun setPassword() {
        binding.applyButton.setOnClickListener {
            val password: String = binding.passwordEditText.text.toString().trim()
            val secretWord: String = binding.wordEditText.text.toString().trim()

            if (TextUtils.isEmpty(password)) {
                binding.passwordEditText.error = getString(R.string.fill_field)
            }
            if (TextUtils.isEmpty(secretWord)) {
                binding.wordEditText.error = getString(R.string.fill_field)
            }
            if (password.isNotEmpty() || secretWord.isNotEmpty()) {
                StringPreference.getInstance(requireContext())
                    ?.saveStringData(Constants.CATEGORY_PASSWORD, password)

                StringPreference.getInstance(requireContext())
                    ?.saveStringData(Constants.SECRET_WORD, secretWord)
                passwordInstall()
                dismiss()
            }
        }
    }
}
