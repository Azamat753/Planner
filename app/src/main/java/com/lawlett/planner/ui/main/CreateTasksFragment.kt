package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.data.room.models.Tasks
import com.lawlett.planner.ui.adapter.TaskAdapter
import kotlinx.android.synthetic.main.fragment_create_tasks.*
import org.koin.android.ext.android.inject

class CreateTasksFragment : BaseFragment(R.layout.fragment_create_tasks) {

    private val viewModel by inject<TaskViewModel>()
    private val adapter = TaskAdapter()
    private val args: CreateTasksFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initViewModel()
    }
    private fun initViewModel() {
        insertDataToDataBase(args.category)
        toolbar_title.text=args.category
        viewModel.getCategory(args.category).observe(viewLifecycleOwner, { tasks ->
            adapter.setData(tasks)
        })
    }

    private fun initRecycler() {
        cr_recycler.adapter = adapter
    }

    private fun insertDataToDataBase(category:String) {
        mic_task.setOnClickListener {
            val taskValues = cr_editText.text.toString()
            if (taskValues.isNotEmpty()) {
                val tasks = Tasks(category = category, task = taskValues, isDone = false)
                viewModel.addTask(tasks)
            }
        }
    }
}