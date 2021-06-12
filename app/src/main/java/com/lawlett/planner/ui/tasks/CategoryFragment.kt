package com.lawlett.planner.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
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
        insertDataToDataBase()
    }

    private fun initClickers() {
        binding.addCategoryFab.setOnClickListener { initBottomSheet() }
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

    private fun insertDataToDataBase() {
        val category =
            CategoryModel(
                categoryName = "Дом",
                taskAmount = 243,
                categoryImage = R.drawable.ic_done
            )
        viewModel.addCategory(category)
        adapter.notifyDataSetChanged()
    }

    private fun initAdapter() {
        binding.categoryRecycler.adapter = adapter
        adapter.listener = this
        getDataFromDataBase()
    }

    override fun onClick(model: CategoryModel) {
        openCategory(model.categoryName)
    }

    private fun openCategory(categoryName: String) {
        val pAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
            CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
        pAction.category = categoryName
        findNavController().navigate(pAction)
    }

//    private fun showAmountTasks() {
//        viewModel.getCategoryLiveData("Персональные")
//            .observe(viewLifecycleOwner, Observer { tasks ->
//                if (tasks.isEmpty()) {
//                    binding.personalAmount.text = "0"
//                } else {
//                    binding.personalAmount.text = tasks.size.toString()
//                }
//            })
//        viewModel.getCategoryLiveData("Работа").observe(viewLifecycleOwner, Observer { tasks ->
//            if (tasks.isEmpty()) {
//                binding.workAmount.text = "0"
//            } else {
//                binding.workAmount.text = tasks.size.toString()
//            }
//        })
//        viewModel.getCategoryLiveData("Встречи").observe(viewLifecycleOwner, Observer { tasks ->
//            if (tasks.isEmpty()) {
//                binding.meetTaskAmount.text = "0"
//            } else {
//                binding.meetTaskAmount.text = tasks.size.toString()
//            }
//        })
//        viewModel.getCategoryLiveData("Дом").observe(viewLifecycleOwner, Observer { tasks ->
//            if (tasks.isEmpty()) {
//                binding.homeTaskAmount.text = "0"
//            } else {
//                binding.homeTaskAmount.text = tasks.size.toString()
//            }
//        })
//        viewModel.getCategoryLiveData("Приватные").observe(viewLifecycleOwner, Observer { tasks ->
//            if (tasks.isEmpty()) {
//                binding.privateTaskAmount.text = "0"
//            } else {
//                binding.privateTaskAmount.text = tasks.size.toString()
//            }
//        })
//    }

//
//    private fun onBackPress() {
//        val onBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                findNavController().navigateUp()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner, onBackPressedCallback
//        )
//    }
}
