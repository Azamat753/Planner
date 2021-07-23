package com.lawlett.planner.ui.dialog.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.IconModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.databinding.CreateCategoryBottomSheetDialogBinding
import com.lawlett.planner.extensions.showToast
import com.lawlett.planner.ui.adapter.IconAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import org.koin.android.ext.android.inject

class CreateCategoryBottomSheetDialog :
    BaseBottomSheetDialog<CreateCategoryBottomSheetDialogBinding>(
        CreateCategoryBottomSheetDialogBinding::inflate
    ), BaseAdapter.IBaseAdapterClickListener<IconModel> {
    var icon: Int = 0
    val viewModel by inject<CategoryViewModel>()
    var isImageChoose: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
    }

    private fun initClickers() {
        binding.iconButton.setOnClickListener { iconPickerDialog() }
        binding.applyButton.setOnClickListener { insertCategory() }
    }

    private fun iconPickerDialog() {
        val adapter = IconAdapter()
        adapter.listener = this
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_icon)
        val recyclerView = dialog.findViewById(R.id.icon_recycler) as RecyclerView
        recyclerView.adapter = adapter
        adapter.setData(fillIcons())
        if (isImageChoose) {
            dialog.cancel()
        }
        dialog.show()
    }

    private fun fillIcons(): List<IconModel> {
        val list: ArrayList<IconModel> = ArrayList()
        list.add(IconModel(R.drawable.ic_work))
        list.add(IconModel(R.drawable.ic_done))
        list.add(IconModel(R.drawable.ic_home))
        list.add(IconModel(R.drawable.ic_hamburger))
        list.add(IconModel(R.drawable.ic_done))
        list.add(IconModel(R.drawable.ic_person))
        list.add(IconModel(R.drawable.ic_choose_image))
        list.add(IconModel(R.drawable.ic_today))
        list.add(IconModel(R.drawable.ic_camera))
        list.add(IconModel(R.drawable.ic_pen))
        list.add(IconModel(R.drawable.ic_notification))
        list.add(IconModel(R.drawable.ic_mic))
        list.add(IconModel(R.drawable.ic_date_white))
        return list
    }

    private fun insertCategory() {
        when {
            icon == 0 -> {
                binding.iconButton.error = getString(R.string.choose_image)
            }
            binding.titleEditText.text.toString().isEmpty() -> {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
            else -> {
                val category = CategoryModel(
                    categoryImage = icon,
                    categoryName = binding.titleEditText.text.toString(),
                    taskAmount = 0
                )
                dismiss()
                viewModel.addCategory(category)
            }
        }
    }

    override fun onClick(model: IconModel) {
        icon = model.icon
        isImageChoose = true
        requireContext().showToast("isClicked")
    }
}