package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseFragment(R.layout.fragment_category) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPress()
        openCategories()

    }

    private fun openCategories() {
        personconst.setOnClickListener {
            val pAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
                CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
            pAction.category =getString(R.string.personal)
            findNavController().navigate(pAction)
        }
            workconst.setOnClickListener {
                val wAction : CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment=
                    CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
                wAction.category=getString(R.string.work)
                findNavController().navigate(wAction)
            }
            meetconst.setOnClickListener {
                val mAction : CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment=
                    CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
                mAction.category=getString(R.string.meets)
                findNavController().navigate(mAction)
            }
            homeconst.setOnClickListener {
                val hAction : CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment=
                    CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
                hAction.category=getString(R.string.home)
                findNavController().navigate(hAction)
            }
            privateconst.setOnClickListener {
                val prAction : CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment=
                    CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment()
                prAction.category =getString(R.string.privates)
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
