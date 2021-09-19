package com.lawlett.planner.ui.tasks

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.databinding.FragmentCategoryBinding
import com.lawlett.planner.extensions.explosionView
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.ui.adapter.CategoryAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.CreateCategoryBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.SwipeHelper
import org.koin.android.ext.android.inject

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<CategoryModel>,
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<CategoryModel> {
    private val viewModel by inject<CategoryViewModel>()
    private val adapter = CategoryAdapter()
    var listModel: List<CategoryModel>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
    }

    private fun initClickers() {
        binding.addCategoryFab.setOnClickListener { initBottomSheet() }
        backClick()
    }

    private fun getDataFromDataBase() {
        viewModel.getCategoryLiveData().observe(viewLifecycleOwner, { category ->
            if (category.isNotEmpty()) {
                adapter.setData(category)
                listModel = category
            }
        })
    }
    private fun initBottomSheet() {
        val bottomDialog = CreateCategoryBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun initAdapter() {
        binding.categoryRecycler.adapter = adapter
        adapter.listener = this
        adapter.longListenerWithModel = this
        getDataFromDataBase()
    }

    override fun onClick(model: CategoryModel, position: Int) {
        openCategory(model)
    }

    private fun openCategory(model: CategoryModel) {
        val pAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
            CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment(model)
        findNavController().navigate(pAction)
    }

    override fun onLongClick(model: CategoryModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete = dialog.findViewById<TextView>(R.id.delete_button)
        val edit = dialog.findViewById<TextView>(R.id.edit_button)

        delete.setOnClickListener {
            deleteCategory(position, model)
            dialog.dismiss()
        }
        edit.setOnClickListener {
            editCategory(position)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteCategory(
        position: Int,
        model: CategoryModel
    ) {
        binding.categoryRecycler.findViewHolderForAdapterPosition(
            position
        )?.itemView?.explosionView(explosionField)
        viewModel.delete(model)
        if (position == 0) {
            findNavController().navigate(R.id.category_fragment)
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    private fun editCategory(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.CATEGORY_MODEL, listModel?.get(position))
        val bottomDialog = CreateCategoryBottomSheetDialog()
        bottomDialog.arguments = bundle
        bottomDialog.show(
            requireActivity().supportFragmentManager,
            Constants.UPDATE_MODEL
        )
    }
}
