package com.lawlett.planner.ui.main

import android.app.AlarmManager
import android.os.Bundle
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.databinding.FragmentCreateEventBinding
import java.util.*

class CreateEventFragment : BaseFragment<FragmentCreateEventBinding>(FragmentCreateEventBinding::inflate) {
    var currentDataString: String? = null
    var mAlarm: AlarmManager? = null
    var time: Long = 0
    var baseCalendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}