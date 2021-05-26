package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.data.room.models.Tasks
import com.lawlett.planner.databinding.FragmentCreateTasksBinding
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.extensions.clearField
import com.lawlett.planner.ui.adapter.TaskAdapter
import kotlinx.android.synthetic.main.fragment_create_tasks.*
import org.koin.android.ext.android.inject
import java.util.*

class CreateTasksFragment :
    BaseFragment<FragmentCreateTasksBinding>(FragmentCreateTasksBinding::inflate),TaskAdapter.Listener {

    private val viewModel by inject<TaskViewModel>()
    private val adapter = TaskAdapter(this)
    private val args: CreateTasksFragmentArgs by navArgs()
    lateinit var taskModel: Tasks
    var currentDone: Int = 0
    var listTasks: List<Tasks>? = null
    lateinit var touchHelper: ItemTouchHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initViewModel()
        swipe()
        drag()
    }
    private fun refreshList() {

    }

    private fun drag() {
        touchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                Collections.swap(listTasks, sourcePosition, targetPosition)
                adapter.notifyItemMoved(sourcePosition, targetPosition)
                refreshList()
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ) {
                super.clearView(recyclerView, viewHolder)
                listTasks?.let { viewModel.updatePosition(it) }
            }
        })
        touchHelper.attachToRecyclerView(cr_recycler)
    }

    private fun swipe() {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    listTasks?.get(viewHolder.adapterPosition)?.let { viewModel.delete(it) }
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(cr_recycler)
    }

    private fun initViewModel() {
        insertDataToDataBase(args.category)
        toolbar_title.text = args.category
        viewModel.getCategoryLiveData(args.category).observe(viewLifecycleOwner, Observer{ tasks ->
            if (tasks.isNotEmpty()) {
                adapter.setData(tasks)
                listTasks = tasks
                currentDone = 0
                tasks.forEach { if (it.isDone) currentDone++ }
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
                cr_editText.clearField()
            }
        }
    }

    override fun onItemClick(pos: Int) {
        taskModel = listTasks!![pos]
        if (!taskModel.isDone) {
            taskModel.isDone = true
            incrementDone()
        } else {
            taskModel.isDone = false
            decrementDone()
        }
        viewModel.update(taskModel)
    }

    private fun decrementDone() {
        currentDone--
        taskModel.doneAmount = currentDone
        viewModel.update(taskModel)
    }

    private fun incrementDone() {
        currentDone++
        taskModel.doneAmount = currentDone
        viewModel.update(taskModel)
    }
}