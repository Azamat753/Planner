package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment

class ProgressFragment : BaseFragment(R.layout.fragment_progress) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
           requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}