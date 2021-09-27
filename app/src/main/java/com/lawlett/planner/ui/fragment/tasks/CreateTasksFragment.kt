package com.lawlett.planner.ui.fragment.tasks

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.navArgs
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.AchievementModel
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.databinding.FragmentCreateTasksBinding
import com.lawlett.planner.extensions.clearField
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.ui.adapter.CheckListener
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
    CheckListener, BaseAdapter.IBaseAdapterLongClickListenerWithModel<TasksModel> {

    private val viewModel by inject<TaskViewModel>()
    private val categoryViewModel by inject<CategoryViewModel>()
    private val achievementViewModel by inject<AchievementViewModel>()
    private lateinit var adapter: TaskAdapter
    private val args: CreateTasksFragmentArgs by navArgs()
    var listTasks: List<TasksModel>? = null
    var taskAmount: Int = 0
    var categoryName = ""
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
        overrideBackClick()
    }

    override fun onStop() {
        super.onStop()
        clearAnimations(binding.viewKonfetti, binding.achievementView)
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

    private fun initViewModel() {
        insertDataToDataBase(categoryName)
        viewModel.getCategoryLiveData(categoryName).observe(viewLifecycleOwner, { tasks ->
            if (tasks.isNotEmpty()) {
                adapter.setData(tasks as List<TasksModel>)
                taskAmount = tasks.size
                doneTaskAmount = 0
                listTasks = tasks
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
        Handler().postDelayed({
            val getModel: CategoryModel = args.model
            val model = CategoryModel(
                taskAmount = taskAmount,
                categoryIcon = getModel.categoryIcon,
                id = getModel.id,
                categoryName = getModel.categoryName,
                doneTaskAmount = doneTaskAmount
            )
            categoryViewModel.update(model)
        }, 100)
    }

    private fun initRecycler() {
        adapter = TaskAdapter(this)
        adapter.longListenerWithModel=this
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

    private fun isFromProgress(): Boolean {
        return arguments?.getBoolean(Constants.IS_PROGRESS) == true
    }

    private fun overrideBackClick() {
        if (isFromProgress()) {
            backToProgress()
        } else {
            onBackPressOverride(R.id.category_fragment)
        }
    }

    override fun checkClick(model: TasksModel) {
        if (!model.isDone) {
            model.isDone = true
            incrementDone(model)
        } else {
            model.isDone = false
            decrementDone(model)
        }
        updateCategoryTaskAmount()
    }

    override fun onLongClick(model: TasksModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete = dialog.findViewById<Button>(R.id.delete_button)
        val edit: Button = dialog.findViewById(R.id.edit_button)
        edit.gone()
        delete.setOnClickListener {
            explosionField.explode(itemView)
            viewModel.delete(model)
            dialog.dismiss()
        }
        dialog.show()
    }

}