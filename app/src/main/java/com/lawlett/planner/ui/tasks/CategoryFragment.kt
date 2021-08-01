package com.lawlett.planner.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.databinding.FragmentCategoryBinding
import com.lawlett.planner.ui.adapter.CategoryAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.CreateCategoryBottomSheetDialog
import org.koin.android.ext.android.inject

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<CategoryModel> {
    private val viewModel by inject<CategoryViewModel>()
    private val adapter = CategoryAdapter()
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
        getDataFromDataBase()
    }

    override fun onClick(model: CategoryModel) {
        model.categoryName?.let { openCategory(it) }
    }

    private fun openCategory(categoryName: String) {
        val pAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
            CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
        pAction.category = categoryName
        findNavController().navigate(pAction)
    }
}
