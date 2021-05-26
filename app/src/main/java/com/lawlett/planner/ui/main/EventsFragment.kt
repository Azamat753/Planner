package com.lawlett.planner.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.databinding.FragmentEventsBinding
import com.lawlett.planner.extensions.toastShow
import com.oscarvera.calendarhorizontal.HorizontalCalendar
import com.oscarvera.calendarhorizontal.data.BasicStyle
import com.oscarvera.calendarhorizontal.interfaz.OnClickDateCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendarView
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_events.*
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

class EventsFragment : OnClickDateCalendar, BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHorCalendar()
        initListeners()

    }
    private fun initListeners() {
        add_quick_btn.setOnClickListener {
            findNavController().navigate(R.id.action_events_fragment_to_createEventFragment)
        }
    }
   private fun initHorCalendar() {

           val startDate = Calendar.getInstance()
           startDate.add(Calendar.MONTH, -1)
           val endDate = Calendar.getInstance()
           endDate.add(Calendar.MONTH, 1)

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

           override fun onCalendarScroll(calendarView: HorizontalCalendarView, dx: Int, dy: Int) {}

           @RequiresApi(api = Build.VERSION_CODES.O)
           @SuppressLint("LogNotTimber")
           override fun onDateLongClicked(date: Calendar, position: Int): Boolean {
               return true
           }
           }

   }
}