package com.lawlett.planner.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.android.ext.android.inject

class CategoryFragment : BaseFragment(R.layout.fragment_category) {
    private val viewModel by inject<TaskViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPress()
        openCategories()
        showAmountTasks()
    }

    private fun showAmountTasks() {
        viewModel.getCategoryLiveData("Персональные").observe(viewLifecycleOwner, { tasks ->
            if (tasks.isNotEmpty()) {
                personal_amount.text = tasks.size.toString()
            }
        })
        viewModel.getCategoryLiveData("Работа").observe(viewLifecycleOwner, { tasks ->
            if (tasks.isNotEmpty()) {
                if (tasks.isEmpty()){
                    work_amount.text ="0"
                }else{
                 work_amount.text=tasks.size.toString()
                }
            }
        })
        viewModel.getCategoryLiveData("Встречи").observe(viewLifecycleOwner, { tasks ->
            if (tasks.isNotEmpty()) {
                meet_task_amount.text = tasks.size.toString()
            }
        })
        viewModel.getCategoryLiveData("Дом").observe(viewLifecycleOwner, { tasks ->
            if (tasks.isNotEmpty()) {
                home_task_amount.text = tasks.size.toString()
            }
        })
        viewModel.getCategoryLiveData("Приватные").observe(viewLifecycleOwner, { tasks ->
            Log.e("ololo", "showAmountTasks: "+tasks.size )
            if (tasks.isNotEmpty()) {
                if (tasks.isEmpty()) {
                    private_task_amount.text = "0"
                } else {
                    private_task_amount.text = tasks.size.toString()
                }
            }
        })
    }

    private fun openCategories() {
        personconst.setOnClickListener {
            val pAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            pAction.category = getString(R.string.personal)
            findNavController().navigate(pAction)
        }
        workconst.setOnClickListener {
            val wAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            wAction.category = getString(R.string.work)
            findNavController().navigate(wAction)
        }
        meetconst.setOnClickListener {
            val mAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            mAction.category = getString(R.string.meets)
            findNavController().navigate(mAction)
        }
        homeconst.setOnClickListener {
            val hAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            hAction.category = getString(R.string.home)
            findNavController().navigate(hAction)
        }
        privateconst.setOnClickListener {
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
