package com.lawlett.planner.ui.progress

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.*
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.databinding.FragmentProgressBinding
import com.lawlett.planner.ui.adapter.*
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.ChooseTimeBottomSheetDialog
import com.lawlett.planner.ui.dialog.fragment.CreateEventBottomSheetDialog
import com.lawlett.planner.utils.Constants
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgressFragment :
    BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private val ideaViewModel by inject<IdeaViewModel>()
    private val habitViewModel by inject<HabitViewModel>()
    private val categoryViewModel by inject<CategoryViewModel>()
    private val eventViewModel by inject<EventViewModel>()
    private var listIdea: List<IdeaModel> = ArrayList()
    private var listHabit: List<HabitModel> = ArrayList()
    private var listEvent: List<EventModel> = ArrayList()
    private var listTask: List<CategoryModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backClickFinish()
        initCategoryAdapter()
        initIdeaProgressAdapter()
        initHabitAdapter()
        initHorizontalCalendar()
        initEventProgressAdapter()
//        initMainAdapter()
    }

    private fun initMainAdapter() {
        val adapter = MainAdapter(fillMainList())
        binding.mainRecycler.adapter = adapter

    }

    private fun initHorizontalCalendar() {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 3)
        val horizontalCalendar = devs.mulham.horizontalcalendar.HorizontalCalendar.Builder(
            activity, R.id.calendarView
        ).range(startDate, endDate)
            .datesNumberOnScreen(5).defaultSelectedDate(startDate)
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {

            }

            override fun onCalendarScroll(
                calendarView: devs.mulham.horizontalcalendar.HorizontalCalendarView?,
                dx: Int,
                dy: Int
            ) {
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("LogNotTimber", "SimpleDateFormat")
            override fun onDateLongClicked(date: Calendar, position: Int): Boolean {
                val dateFormat = SimpleDateFormat("E, MMM d")
                val chooseDate = dateFormat.format(date.time)
                val bottomDialog = CreateEventBottomSheetDialog()
                val bundle = Bundle()
                bundle.putString("date", chooseDate)
                bottomDialog.arguments = bundle
                bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
                return true
            }
        }
    }


    private fun backClickFinish() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    private fun fillMainList(): List<MainListModel> {
        val list: ArrayList<MainListModel> = ArrayList()
        list.add(MainListModel(listIdea, listHabit, listEvent, listTask))
        return list
    }


    private fun initIdeaProgressAdapter() {
        val adapter = IdeaProgressAdapter()
        binding.ideasProgressRecycler.adapter = adapter
        ideaViewModel.getIdeasLiveData().observe(viewLifecycleOwner, { ideas ->
            adapter.setData(ideas)
            listIdea = ideas
        })
    }

    private fun initEventProgressAdapter() {
        val adapter = EventAdapter()
        binding.eventProgressRecycler.adapter = adapter
        eventViewModel.getData().observe(viewLifecycleOwner, { events ->
            adapter.setData(events)
            listEvent = events
        })
    }

    private fun initHabitAdapter() {
        val adapter = HabitAdapter()
        binding.habitRecycler.adapter = adapter
        habitViewModel.getHabitsLiveData().observe(viewLifecycleOwner, { habits ->
            adapter.setData(habits)
            listHabit = habits
        })
    }

    private fun initCategoryAdapter() {
        val adapter = CategoryAdapter()
        binding.categoryRecycler.adapter = adapter
        categoryViewModel.getCategoryLiveData().observe(viewLifecycleOwner, { category ->
            adapter.setData(category)
            listTask = category
        })
    }
}