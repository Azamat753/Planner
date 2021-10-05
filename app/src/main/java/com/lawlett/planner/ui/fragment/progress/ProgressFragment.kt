package com.lawlett.planner.ui.fragment.progress

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.databinding.FragmentProgressBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.EventAdapter
import com.lawlett.planner.ui.adapter.HabitAdapter
import com.lawlett.planner.ui.adapter.TaskProgressAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateEventBottomSheetDialog
import com.lawlett.planner.ui.dialog.CreateTimetableBottomSheetDialog
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
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
        addFalseDataForExample()
        showSpotlight()
    }

    private fun addFalseDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.PROGRESS_EXAMPLE_DATA) == false
        ) {
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            var myHour = ""
            var myMinute = ""

            myHour = if (hour.toString().count() == 1) {
                "0$hour"
            } else {
                hour.toString()
            }

            myMinute = if (minute.toString().count() == 1) {
                "0$minute"
            } else {
                minute.toString()
            }

            val habitModel = HabitModel(
                title = getString(R.string.wake_up_morning),
                currentDay = 0,
                allDays = "21",
                icon = "\uD83D\uDE06"
            )
            val habitModel2 =
                HabitModel(
                    title = getString(R.string.read_by_minutes),
                    currentDay = 0,
                    allDays = "30",
                    icon = "⏳"
                )

            val categoryModel = CategoryModel(
                categoryName = getString(R.string.health),
                categoryIcon = "\uD83C\uDF4E",
                taskAmount = 0,
                doneTaskAmount = 0
            )
            val categoryModel2 = CategoryModel(
                categoryName = getString(R.string.targets),
                categoryIcon = "\uD83C\uDFAF",
                taskAmount = 0,
                doneTaskAmount = 0
            )

            val eventModel = EventModel(
                title = getString(R.string.download_app),
                date = " ${theMonth(month, requireContext())}  $day",
                time = "$myHour : $myMinute",
                color = R.color.dark_blue_theme,
                remindTime = ""
            )
            val eventModel2 = EventModel(
                title = getString(R.string.look_how_it_work),
                date = " ${theMonth(month, requireContext())}  $day",
                time = "$myHour : $myMinute",
                color = R.color.red_theme,
                remindTime = ""
            )

            habitViewModel.insertData(habitModel)
            habitViewModel.insertData(habitModel2)
            categoryViewModel.addCategory(categoryModel)
            categoryViewModel.addCategory(categoryModel2)
            eventViewModel.addData(eventModel)
            eventViewModel.addData(eventModel2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.PROGRESS_EXAMPLE_DATA, true)
        }
    }

    private fun showSpotlight() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.PROGRESS_INSTRUCTION) == false
        ) {
            val targets = ArrayList<Target>()
            val root = FrameLayout(requireContext())
            val first = layoutInflater.inflate(R.layout.layout_target, root)
            val view = View(requireContext())
            Handler().postDelayed({

                val zeroSpot = setSpotLightTarget(
                    view,
                    first,
                    getString(R.string.before_start) + "\n " + getString(R.string.two_tools) + "\n " + getString(
                        R.string.bottom_and_side
                    ) + " \n " + getString(R.string.side_open_by_click) + "\n " + getString(R.string.after_instruction) + "\n " + getString(
                        R.string.go
                    )
                )
                val firstSpot = setSpotLightTarget(
                    binding.calendarCard,
                    first,
                    getString(R.string.you_calendar) + " \n " + getString(R.string.show_day) + " \n " + getString(
                        R.string.set_event_new_task
                    )
                )
                val secondSpot = setSpotLightTarget(
                    binding.categoryRecycler,
                    first,
                    getString(R.string.next_you_cards) + " \n " + getString(R.string.here_show)
                            + " \n " + getString(R.string.click_cards_ha_ca)
                )

                val thirdSpot = setSpotLightTarget(
                    view,
                    first,
                    getString(R.string.show_profile) + " \n " + getString(R.string.avater_name) + " \n " + getString(
                        R.string.lvlup_some_action
                    )
                )
                targets.add(zeroSpot)
                targets.add(firstSpot)
                targets.add(secondSpot)
                targets.add(thirdSpot)
                setSpotLightBuilder(requireActivity(), targets, first)
            }, 100)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.PROGRESS_INSTRUCTION, true)
        }
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
                findNavController().navigate(R.id.progress_fragment)
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
            ProgressFragmentDirections.actionProgressFragmentToCreateTasksFragment(model, true)
        findNavController().navigate(pAction)
    }
}