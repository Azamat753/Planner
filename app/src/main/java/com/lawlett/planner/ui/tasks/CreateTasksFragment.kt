package com.lawlett.planner.ui.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.AchievementModel
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.databinding.FragmentCreateTasksBinding
import com.lawlett.planner.extensions.clearField
import com.lawlett.planner.ui.adapter.TaskAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.StringPreference
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.koin.android.ext.android.inject
import java.util.*


class CreateTasksFragment :
    BaseFragment<FragmentCreateTasksBinding>(FragmentCreateTasksBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<TasksModel> {

    private val viewModel by inject<TaskViewModel>()
    private val categoryViewModel by inject<CategoryViewModel>()
    private val achievementViewModel by inject<AchievementViewModel>()
    private val adapter = TaskAdapter()
    private val args: CreateTasksFragmentArgs by navArgs()
    lateinit var taskModel: TasksModel
    var listTasks: List<TasksModel>? = null
    var taskAmount: Int = 0
    var categoryName = ""
    private lateinit var touchHelper: ItemTouchHelper
    private var doneTaskAmount: Int = 0
    private var nowLevel = 0
    private var levelId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryName = args.model.categoryName.toString()
        binding.toolbar.title = categoryName
        initRecycler()
        initViewModel()
        getCurrentLevel()
        swipe()
        drag()
        backPress()
    }

    override fun onStart() {
        super.onStart()
        clearAnimations(binding.viewKonfetti,binding.achievementView)
    }

    private fun burstKonfetti() {
        binding.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12), Size(16, 6f))
            .setPosition(
                binding.viewKonfetti.x + binding.viewKonfetti.width / 2,
                binding.viewKonfetti.y + binding.viewKonfetti.height / 3
            )
            .burst(100)
    }

    private fun rainKonfetti() {
        binding.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
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
        insertDataToDataBase(categoryName)
        viewModel.getCategoryLiveData(categoryName).observe(viewLifecycleOwner, { tasks ->
            if (tasks.isNotEmpty()) {
                adapter.setData(tasks as List<TasksModel>)
                taskAmount = tasks.size
                listTasks = tasks
                doneTaskAmount = 0
                tasks.forEach { if (it.isDone) doneTaskAmount++ }
            }
        })
        adapter.notifyDataSetChanged()
    }


    private fun getCurrentLevel() {
        achievementViewModel.getData().observe(viewLifecycleOwner, { level ->
            if (level.isNotEmpty()) {
                nowLevel = level[0].level
                levelId = level[0].id!!
            } else {
                val model = AchievementModel(level = 0)
                achievementViewModel.insertData(model)
            }
        })
    }

    private fun rewardAnAchievement(completeTask: Int) {
        if (completeTask % 5 == 0) {
            StringPreference.getInstance(requireContext())
                ?.saveStringData(Constants.COMPLETE_TASK, completeTask.toString())
            nowLevel += 1
            val model = AchievementModel(level = nowLevel, id = levelId)
            achievementViewModel.update(model)
            binding.achievementView.show("Поздравляем!", "Уровень $nowLevel")
        }
    }


    private fun updateCategoryTaskAmount() {
        val getModel: CategoryModel = args.model
        val model = CategoryModel(
            taskAmount = listTasks?.size?.plus(1),
            categoryIcon = getModel.categoryIcon,
            id = getModel.id,
            categoryName = getModel.categoryName
        )
        categoryViewModel.update(model)
    }

    private fun initRecycler() {
        binding.crRecycler.adapter = adapter
        adapter.listener = this
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
                binding.crEditText.clearField()
                burstKonfetti()
            }
            updateCategoryTaskAmount()
        }
    }

    private fun decrementDone(model: TasksModel) {
        doneTaskAmount--
        model.doneAmount = doneTaskAmount
        viewModel.update(model)
    }

    private fun incrementDone(model: TasksModel) {
        rainKonfetti()
        doneTaskAmount++
        model.doneAmount = doneTaskAmount
        viewModel.update(model)

        val amount =
            StringPreference.getInstance(requireContext())?.getStringData(Constants.COMPLETE_TASK)
        if (!amount.equals(doneTaskAmount.toString())) {
            rewardAnAchievement(doneTaskAmount)
        }
    }

    private fun backPress() {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.category_fragment)
        }
    }

    override fun onClick(model: TasksModel,position:Int) {
        if (!model.isDone) {
            model.isDone = true
            incrementDone(model)
        } else {
            model.isDone = false
            decrementDone(model)
        }
    }
}