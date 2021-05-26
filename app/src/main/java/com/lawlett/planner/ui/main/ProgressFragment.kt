package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.databinding.FragmentProgressBinding

class ProgressFragment : BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {

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