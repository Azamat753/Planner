package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.extensions.toastShow
import com.oscarvera.calendarhorizontal.HorizontalCalendar
import com.oscarvera.calendarhorizontal.data.BasicStyle
import com.oscarvera.calendarhorizontal.interfaz.OnClickDateCalendar
import kotlinx.android.synthetic.main.fragment_events.*
import java.util.*
import kotlin.collections.ArrayList

class EventsFragment : OnClickDateCalendar, BaseFragment(R.layout.fragment_events) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalendar()
        initListeners()

    }
    private fun initListeners() {
        add_quick_btn.setOnClickListener {
            findNavController().navigate(R.id.action_events_fragment_to_createEventFragment)
        }
    }

    private fun initCalendar() {
        var cal1 = Calendar.getInstance()
        var cal2 = Calendar.getInstance()
        var cal3 = Calendar.getInstance()
        cal2.add(Calendar.DAY_OF_YEAR, 4)
        cal1.add(Calendar.DAY_OF_YEAR, 2)
        cal3.add(Calendar.DAY_OF_YEAR, -2)
        var daysSelected = ArrayList<Date>()
        daysSelected.add(cal1.time)
        daysSelected.add(cal3.time)

        view?.let {
            HorizontalCalendar.Build(
                it.findViewById(R.id.calendar_view),
                requireContext(), //Id of the calendar view
                BasicStyle( //Basic Style of the calendar
                    R.color.royalStart,  //Color of the days before the current date
                    R.color.royalStart, //Color of the days after the current date
                    R.color.meetColor
                )
            ).//Color of the current day
            daysInScreen(7).rangeMax(7, HorizontalCalendar.TIMEMEASURE.DAY)
                .build()
        }
    }

    override fun onClickDate(
        date: Date,
        isInExtraRange: Boolean,
        isSelected: Boolean,
        isDayPast: Boolean
    ) {
        requireContext().toastShow("isClicked")
    }

}