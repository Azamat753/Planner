package com.lawlett.planner.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.databinding.FragmentCategoryBinding
import com.lawlett.planner.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate) {
    private val viewModel by inject<TaskViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPress()
        openCategories()
        showAmountTasks()
    }

    private fun showAmountTasks() {
        viewModel.getCategoryLiveData("Персональные")
            .observe(viewLifecycleOwner, Observer { tasks ->
                if (tasks.isEmpty()) {
                    binding.personalAmount.text = "0"
                } else {
                    binding.personalAmount.text = tasks.size.toString()
                }
            })
        viewModel.getCategoryLiveData("Работа").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                binding.workAmount.text = "0"
            } else {
                binding.workAmount.text = tasks.size.toString()
            }
        })
        viewModel.getCategoryLiveData("Встречи").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                binding.meetTaskAmount.text = "0"
            } else {
                binding.meetTaskAmount.text = tasks.size.toString()
            }
        })
        viewModel.getCategoryLiveData("Дом").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                binding.homeTaskAmount.text = "0"
            } else {
                binding.homeTaskAmount.text = tasks.size.toString()
            }
        })
        viewModel.getCategoryLiveData("Приватные").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                binding.privateTaskAmount.text = "0"
            } else {
                binding.privateTaskAmount.text = tasks.size.toString()
            }
        })
    }

    private fun openCategories() {
        binding.personconst.setOnClickListener {
            val pAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            pAction.category = getString(R.string.personal)
            findNavController().navigate(pAction)
        }
        binding.workconst.setOnClickListener {
            val wAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            wAction.category = getString(R.string.work)
            findNavController().navigate(wAction)
        }
        binding.meetconst.setOnClickListener {
            val mAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            mAction.category = getString(R.string.meets)
            findNavController().navigate(mAction)
        }
        binding.homeconst.setOnClickListener {
            val hAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            hAction.category = getString(R.string.home)
            findNavController().navigate(hAction)
        }
        binding.privateconst.setOnClickListener {
            val prAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            prAction.category = getString(R.string.privates)
            findNavController().navigate(prAction)
        }
    }

    private fun onBackPress() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
    }
}
