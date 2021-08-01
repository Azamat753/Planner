package com.lawlett.planner.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.databinding.FragmentCreateTasksBinding
import com.lawlett.planner.extensions.clearField
import com.lawlett.planner.ui.adapter.TaskAdapter
import com.lawlett.planner.ui.base.BaseFragment
import org.koin.android.ext.android.inject
import java.util.*

class CreateTasksFragment :
    BaseFragment<FragmentCreateTasksBinding>(FragmentCreateTasksBinding::inflate) {

    private val viewModel by inject<TaskViewModel>()
    private val categoryViewModel by inject<CategoryViewModel>()
    private val adapter = TaskAdapter()
    private val args: CreateTasksFragmentArgs by navArgs()
    lateinit var taskModel: TasksModel
    var currentDone: Int = 0
    var listTasks: List<TasksModel>? = null
    var taskAmount: Int = 0
    lateinit var touchHelper: ItemTouchHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initViewModel()
        swipe()
        drag()
        backPress()
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
        touchHelper.attachToRecyclerView(binding.crRecycler)
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
        itemTouchHelper.attachToRecyclerView(binding.crRecycler)
    }

    private fun initViewModel() {
        insertDataToDataBase(args.category)
        binding.toolbar.title = args.category
        viewModel.getCategoryLiveData(args.category).observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isNotEmpty()) {
                adapter.setData(tasks as List<TasksModel>)
                taskAmount = tasks.size
                listTasks = tasks
                currentDone = 0
                tasks.forEach { if (it.isDone) currentDone++ }
            }
        })
    }

    private fun updateCategoryTaskAmount() {
            categoryViewModel.getCategoryByName(args.category).observe(viewLifecycleOwner,
                Observer { category ->
                    if (category != null) {
                        category.taskAmount = taskAmount
                        categoryViewModel.update(category)
                    }
                })
    }

    private fun initRecycler() {
        binding.crRecycler.adapter = adapter
    }

    private fun insertDataToDataBase(category: String) {
        binding.addTaskPersonal.setOnClickListener {
            val taskValues = binding.crEditText.text.toString()
            if (taskValues.isNotEmpty()) {
                val tasks = TasksModel(
                    category = category,
                    task = taskValues,
                    isDone = false
                )
                viewModel.addTask(tasks)
                updateCategoryTaskAmount()
                binding.crEditText.clearField()
            }
        }
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

    private fun backPress() {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.category_fragment)
        }
    }
}