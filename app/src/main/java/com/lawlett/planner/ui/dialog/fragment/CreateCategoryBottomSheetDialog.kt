package com.lawlett.planner.ui.dialog.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.IconModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.databinding.CreateCategoryBottomSheetDialogBinding
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.getIcons
import com.lawlett.planner.ui.adapter.IconAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class CreateCategoryBottomSheetDialog :
    BaseBottomSheetDialog<CreateCategoryBottomSheetDialogBinding>(
        CreateCategoryBottomSheetDialogBinding::inflate
    ), BaseAdapter.IBaseAdapterClickListener<IconModel> {
    var icon: String = ""
    val viewModel by inject<CategoryViewModel>()
    private var isImageChoose: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        setDataForUpdate()
    }

    private fun initClickers() {
        binding.iconButton.setOnClickListener { iconPickerDialog() }
        binding.applyButton.setOnClickListener {
            insertOrUpdateCategory()
        }
    }

    private fun iconPickerDialog() {
        val adapter = IconAdapter()
        adapter.listener = this
        val dialog = requireContext().getDialog(R.layout.dialog_icon)
        val titleCard: View = dialog.findViewById(R.id.title_card)
        val title = titleCard.findViewById<TextView>(R.id.title)
        title.text = "Выбор иконки"
        val recyclerView = dialog.findViewById(R.id.icon_recycler) as RecyclerView
        recyclerView.adapter = adapter
        adapter.setData(getIcons())
        if (isImageChoose) {
            dialog.cancel()
        }
        dialog.show()
    }

    fun isUpdate(): Boolean {
        return tag.toString() == Constants.UPDATE_MODEL
    }

    private fun setDataForUpdate() {
        if (isUpdate()) {
            val model: CategoryModel =
                arguments?.getSerializable(Constants.CATEGORY_MODEL) as CategoryModel
            binding.iconTv.text = model.categoryIcon
            binding.title.text = model.categoryName
            binding.titleEditText.setText(model.categoryName)
        }
    }

    private fun insertOrUpdateCategory() {
        val title = binding.titleEditText.text.toString()
        if (isUpdate()) {
            updateCategory(title)
        } else {
            insertCategory(title)
        }
    }

    private fun insertCategory(title: String) {
        when {
            icon == "" -> {
                binding.iconButton.error = getString(R.string.choose_image)
            }
            binding.titleEditText.text.toString().isEmpty() -> {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
            else -> {
                val category = CategoryModel(
                    categoryIcon = icon,
                    categoryName = title,
                    taskAmount = 0
                )
                viewModel.addCategory(category)
                dismiss()
            }
        }
    }

    private fun updateCategory(title: String) {
        val model: CategoryModel =
            arguments?.getSerializable(Constants.CATEGORY_MODEL) as CategoryModel
        val currentIcon = binding.iconTv.text.toString()
        if (binding.titleEditText.text.toString().isEmpty()) {
            binding.titleEditText.error = getString(R.string.fill_field)
        } else {
            val updateModel = CategoryModel(
                id = model.id,
                categoryIcon = currentIcon,
                categoryName = title,
                taskAmount = model.taskAmount
            )
            viewModel.update(updateModel)
            dismiss()
        }
    }

    override fun onClick(model: IconModel,position:Int) {
        icon = model.icon
        isImageChoose = true
        binding.iconTv.text = model.icon
    }
}