package com.lawlett.planner.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import kotlinx.android.synthetic.main.fragment_progress.*
import org.koin.android.ext.android.inject
import com.lawlett.planner.databinding.FragmentProgressBinding

class ProgressFragment : BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private val viewModel by inject<TaskViewModel>()
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
    }

    private fun getData() {
        viewModel.getCategoryLiveData("Персональные").observe(viewLifecycleOwner, Observer{ tasks ->
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

        viewModel.getCategoryLiveData("Работа").observe(viewLifecycleOwner, Observer{ tasks ->
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

        viewModel.getCategoryLiveData("Встречи").observe(viewLifecycleOwner,Observer { tasks ->
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

        viewModel.getCategoryLiveData("Дом").observe(viewLifecycleOwner,Observer { tasks ->
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

        viewModel.getCategoryLiveData("Приватные").observe(viewLifecycleOwner,Observer { tasks ->
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
        todo_amount.text = allTasksAmount.toString()
        complete_task_amount.text = allTasksDoneAmount.toString()

        allTask_percent.text = "$allTasksPercent %"
        personal_percent.text = "$personalPercent %"
        work_percent.text = "$workAmountPercent %"
        meetTask_percent.text = "$meetAmountPercent %"
        homeTask_percent.text = "$homeAmountPercent %"
        privateTask_percent.text = "$privateAmountPercent %"

        allTask_progress.progress = allTasksDoneAmount
        personal_progress.progress = personalDoneAmount
        work_progress.progress = workDoneAmount
        meetTask_progress.progress = meetDoneAmount
        homeTask_progress.progress = homeDoneAmount
        private_progress.progress = privateDoneAmount

        allTask_progress.max = allTasksAmount
        personal_progress.max = personalAmount
        work_progress.max = workAmount
        meetTask_progress.max = meetAmount
        homeTask_progress.max = homeAmount
        private_progress.max = privateAmount
    }
}