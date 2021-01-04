package com.lawlett.planner.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.data.room.models.Tasks
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.extensions.toastShow
import com.lawlett.planner.ui.adapter.TaskAdapter
import kotlinx.android.synthetic.main.fragment_create_tasks.*
import org.koin.android.ext.android.inject

class CreateTasksFragment : BaseFragment(R.layout.fragment_create_tasks), TaskAdapter.Listener {

    private val viewModel by inject<TaskViewModel>()
    private val adapter = TaskAdapter(this)
    private val args: CreateTasksFragmentArgs by navArgs()
    lateinit var taskModel: Tasks
    var currentDone: Int = 0
    var previousDone: Int = 0
    var updateDone: Int = 0
    lateinit var listTasks: List<Tasks>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initViewModel()
    }

    private fun initViewModel() {
        insertDataToDataBase(args.category)
        toolbar_title.text = args.category
        viewModel.getCategoryLiveData(args.category).observe(viewLifecycleOwner, { tasks ->
            if (tasks.isNotEmpty()) {
                adapter.setData(tasks)
                listTasks = tasks
                currentDone = 0
                tasks.forEach { if (it.isDone) currentDone ++ }
            }
        })
    }

    private fun initRecycler() {
        cr_recycler.adapter = adapter
    }

    private fun insertDataToDataBase(category: String) {
        mic_task.setOnClickListener {
            val taskValues = cr_editText.text.toString()
            if (taskValues.isNotEmpty()) {
                val tasks = Tasks(category = category,
                    task = taskValues,
                    isDone = false)
                viewModel.addTask(tasks)
            }
        }
    }

    override fun onItemClick(pos: Int) {
        taskModel = listTasks[pos]
        if (!taskModel.isDone) {
            taskModel.isDone = true
            incrementDone()
        } else {
            taskModel.isDone = false
            decrementDone()
        }
        viewModel.update(taskModel)
        requireContext().toastShow(taskModel.doneAmount.toString())
    }

    private fun decrementDone() {
        currentDone --
        taskModel.doneAmount = currentDone
        viewModel.update(taskModel)
        Log.e("ololo", "onItemClick: " + taskModel.doneAmount)
    }

    private fun incrementDone() {
        currentDone ++
        taskModel.doneAmount = currentDone
        Log.e("ololo", "onItemClick: " + taskModel.doneAmount)
        viewModel.update(taskModel)
    }
}