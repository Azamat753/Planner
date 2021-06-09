package com.lawlett.planner.ui.progress

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.databinding.FragmentProgressBinding
import com.lawlett.planner.ui.adapter.HabitAdapter
import com.lawlett.planner.ui.adapter.IdeaProgressAdapter
import com.lawlett.planner.ui.adapter.TaskProgressAdapter
import com.lawlett.planner.ui.base.BaseFragment
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class ProgressFragment : BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private val viewModel by inject<TaskViewModel>()
    private val ideaViewModel by inject<IdeaViewModel>()
    private val habitViewModel by inject<HabitViewModel>()
    private var allTasksDoneAmount: Int = 0
    private var personalDoneAmount: Int = 0
    private var workDoneAmount: Int = 0
    private var homeDoneAmount: Int = 0
    private var meetDoneAmount: Int = 0
    private var privateDoneAmount: Int = 0
    private var allTasksAmount: Int = 0
    private var personalAmount: Int = 0
    private var workAmount: Int = 0
    private var meetAmount: Int = 0
    private var homeAmount: Int = 0
    private var privateAmount: Int = 0

    private var allTasksPercent: Int = 0
    private var personalPercent: Int = 0
    private var workAmountPercent: Int = 0
    private var meetAmountPercent: Int = 0
    private var homeAmountPercent: Int = 0
    private var privateAmountPercent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initAdapter()
        initIdeaProgressAdapter()
        initHabitAdapter()
        initHorizontalCalendar()
    }

    private fun initHorizontalCalendar() {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 3)
        try {
            val horizontalCalendar = devs.mulham.horizontalcalendar.HorizontalCalendar.Builder(
                activity, R.id.calendarView
            ).range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build()

            horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
                @SuppressLint("LogNotTimber", "NewApi")
                override fun onDateSelected(date: Calendar, position: Int) {
                    //                Intent intent = new Intent(getContext(), TodayEvent.class);
                    //                intent.putExtra("month",String.valueOf(date.getTime().getMonth()));
                    //                intent.putExtra("day",String.valueOf(date.getTime().getDate()));
                    //                startActivity(intent);
                }

                override fun onCalendarScroll(
                    calendarView: devs.mulham.horizontalcalendar.HorizontalCalendarView?,
                    dx: Int,
                    dy: Int
                ) {
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint("LogNotTimber")
                override fun onDateLongClicked(date: Calendar, position: Int): Boolean {
                    return true
                }
            }
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }

    }

    private fun initIdeaProgressAdapter() {
        val adapter = IdeaProgressAdapter()
        binding.ideasProgressRecycler.adapter = adapter
        ideaViewModel.getIdeasLiveData().observe(viewLifecycleOwner, { ideas ->
            adapter.setData(ideas)
        })
    }

    private fun initHabitAdapter() {
        val adapter = HabitAdapter()
        binding.habitRecycler.adapter=adapter
        habitViewModel.getHabitsLiveData().observe(viewLifecycleOwner, { habits ->
            adapter.setData(habits)
        })
    }

    private fun initAdapter() {
        val adapter = TaskProgressAdapter()
        binding.tasksRecycler.adapter = adapter
        val model: ArrayList<TasksModel> = ArrayList()
        model.add(TasksModel(category = "Работа", doneAmount = 12, task = "das", isDone = true))
        model.add(
            TasksModel(
                category = "Персональные",
                doneAmount = 62,
                task = "das",
                isDone = true
            )
        )
        model.add(TasksModel(category = "Встречи", doneAmount = 5, task = "das", isDone = true))
        model.add(TasksModel(category = "Дом", doneAmount = 412, task = "das", isDone = true))
        adapter.setData(model)
    }

    private fun getData() {
        viewModel.getCategoryLiveData("Персональные")
            .observe(viewLifecycleOwner, Observer { tasks ->
                if (tasks.isEmpty()) {
                    personalAmount = 0
                    personalDoneAmount = 0
                } else {
                    personalAmount = tasks.size
                    for (t in tasks) {
                        if (t.isDone) {
                            personalDoneAmount++
                        }
                    }
                }
            })

        viewModel.getCategoryLiveData("Работа").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                workAmount = 0
                workDoneAmount = 0
            } else {
                workAmount = tasks.size
                for (t in tasks) {
                    if (t.isDone) {
                        workDoneAmount++
                    }
                }
            }
        })

        viewModel.getCategoryLiveData("Встречи").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                meetAmount = 0
                meetDoneAmount = 0
            } else {
                meetAmount = tasks.size
                for (t in tasks) {
                    if (t.isDone) {
                        meetDoneAmount++
                    }
                }
            }
        })

        viewModel.getCategoryLiveData("Дом").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                homeAmount = 0
                homeDoneAmount = 0
            } else {
                homeAmount = tasks.size
                for (t in tasks) {
                    if (t.isDone) {
                        homeDoneAmount++
                    }
                }
            }
        })

        viewModel.getCategoryLiveData("Приватные").observe(viewLifecycleOwner, Observer { tasks ->
            if (tasks.isEmpty()) {
                privateAmount = 0
                privateDoneAmount = 0
            } else {
                privateAmount = tasks.size
                for (t in tasks) {
                    if (t.isDone) {
                        privateDoneAmount++
                    }
                }
            }
            countAll()
            countPercent()
            setData()
        })
    }

    private fun countAll() {
        allTasksAmount = personalAmount + workAmount + meetAmount + homeAmount + privateAmount
        allTasksDoneAmount =
            personalDoneAmount + workDoneAmount + meetDoneAmount + homeDoneAmount + privateDoneAmount
    }

    private fun countPercent() {
        try {
            allTasksPercent = allTasksDoneAmount * 100 / allTasksAmount
            personalPercent = personalDoneAmount * 100 / personalAmount
            workAmountPercent = workDoneAmount * 100 / workAmount
            meetAmountPercent = meetDoneAmount * 100 / meetAmount
            homeAmountPercent = homeDoneAmount * 100 / homeAmount
            privateAmountPercent = privateDoneAmount * 100 / privateAmount
        } catch (ar: ArithmeticException) {
            ar.printStackTrace()
        }
    }

    private fun setData() {
//        binding.todoAmount.text = allTasksAmount.toString()
//        binding.completeTaskAmount.text = allTasksDoneAmount.toString()
//
//        binding.allTaskPercent.text = "$allTasksPercent %"
//        binding.personalPercent.text = "$personalPercent %"
//        binding.workPercent.text = "$workAmountPercent %"
//        binding.meetTaskPercent.text = "$meetAmountPercent %"
//        binding.homeTaskPercent.text = "$homeAmountPercent %"
//        binding.privateTaskPercent.text = "$privateAmountPercent %"
//        binding.allTaskProgress.progress = allTasksDoneAmount
//        binding.personalProgress.progress = personalDoneAmount
//        binding.workProgress.progress = workDoneAmount
//        binding.meetTaskProgress.progress = meetDoneAmount
//        binding.homeTaskProgress.progress = homeDoneAmount
//        binding.privateProgress.progress = privateDoneAmount
//        binding.allTaskProgress.max = allTasksAmount
//        binding.personalProgress.max = personalAmount
//        binding.workProgress.max = workAmount
//        binding.meetTaskProgress.max = meetAmount
//        binding.homeTaskProgress.max = homeAmount
//        binding.privateProgress.max = privateAmount
    }
}