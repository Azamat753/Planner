package com.lawlett.planner.ui.fragment.progress

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.*
import com.lawlett.planner.data.room.viewmodels.*
import com.lawlett.planner.databinding.FragmentProgressBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.*
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateEventBottomSheetDialog
import com.lawlett.planner.ui.dialog.CreateTimetableBottomSheetDialog
import com.takusemba.spotlight.Target
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class ProgressFragment :
    BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private val habitViewModel by inject<HabitViewModel>()
    private val categoryViewModel by inject<CategoryViewModel>()
    private val eventViewModel by inject<EventViewModel>()
    private var listHabit: List<HabitModel> = ArrayList()
    private var listEvent: List<EventModel> = ArrayList()
    private var listTask: List<CategoryModel> = ArrayList()
    private val achievementViewModel by inject<AchievementViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backClickFinish()
        initCategoryAdapter()
        initHabitAdapter()

        Handler().postDelayed({
            initHorizontalCalendar()
        }, 1)
        initEventProgressAdapter()

//        val targetHabit: TextView = binding.habitProgress
//        val targets = ArrayList<Target>()
//
//        val root = FrameLayout(requireContext())
//        val first = layoutInflater.inflate(R.layout.layout_target, root)
//
//        setSpotLightTarget(binding.habitProgress, first, targets, "ghf")
//        setSpotLightBuilder(requireActivity(), targets, first)

    }

    override fun onStop() {
        super.onStop()
        clearAnimations(null, binding.achievementView)
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

                val dialog = requireContext().getDialog(R.layout.long_click_dialog)
                val event = dialog.findViewById<Button>(R.id.delete_button)
                val timetable = dialog.findViewById<Button>(R.id.edit_button)
                event.text = getString(R.string.events)
                timetable.text = getString(R.string.timetable)

                event.setOnClickListener {
                    addEvent(date)
                    dialog.dismiss()
                }
                timetable.setOnClickListener {
                    addTimeTable(date)
                    dialog.dismiss()
                }
                dialog.show()
                return true
            }
        }
    }

    private fun addTimeTable(date: Calendar) {
        val day = date.get(Calendar.DAY_OF_WEEK)
        var dayOfWeek = 0
        when (day) {
            Calendar.MONDAY -> dayOfWeek = 0
            Calendar.TUESDAY -> dayOfWeek = 1
            Calendar.WEDNESDAY -> dayOfWeek = 2
            Calendar.THURSDAY -> dayOfWeek = 3
            Calendar.FRIDAY -> dayOfWeek = 4
            Calendar.SATURDAY -> dayOfWeek = 5
            Calendar.SUNDAY -> dayOfWeek = 6
        }

        val bottomDialog = CreateTimetableBottomSheetDialog(null)
        bottomDialog.show(
            requireActivity().supportFragmentManager,
            dayOfWeek.toString()
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun addEvent(date: Calendar) {
        val dayOfWeek = theMonth(date.get(Calendar.MONTH), requireContext())
        val dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
        val chooseDate = "$dayOfWeek $dayOfMonth"
        val bottomDialog = CreateEventBottomSheetDialog()
        val bundle = Bundle()
        bundle.putString("date", chooseDate)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun backClickFinish() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
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
        adapter.listener = object : BaseAdapter.IBaseAdapterClickListener<HabitModel> {
            override fun onClick(model: HabitModel, position: Int) {
                showHabitDayUpDialog(
                    model,
                    position,
                    requireContext(),
                    adapter,
                    habitViewModel,
                    requireActivity(),
                    achievementViewModel,
                    binding.achievementView
                )
            }
        }
        habitViewModel.getHabitsLiveData().observe(viewLifecycleOwner, { habits ->
            adapter.setData(habits)
            listHabit = habits
        })
    }

    private fun initCategoryAdapter() {
        val adapter = TaskProgressAdapter()
        binding.categoryRecycler.adapter = adapter
        adapter.listener = object : BaseAdapter.IBaseAdapterClickListener<CategoryModel> {
            override fun onClick(model: CategoryModel, position: Int) {
                openCategory(model)
            }
        }
        categoryViewModel.getCategoryLiveData().observe(viewLifecycleOwner, { category ->
            adapter.setData(category)
            listTask = category
        })
    }

    private fun openCategory(model: CategoryModel) {
        val pAction: ProgressFragmentDirections.ActionProgressFragmentToCreateTasksFragment =
            ProgressFragmentDirections.actionProgressFragmentToCreateTasksFragment(model,true)
        findNavController().navigate(pAction)
    }
}