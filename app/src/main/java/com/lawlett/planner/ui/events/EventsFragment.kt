package com.lawlett.planner.ui.events

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.databinding.FragmentEventsBinding
import com.lawlett.planner.ui.adapter.EventAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.CreateEventBottomSheetDialog
import org.koin.android.ext.android.inject

class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {
    val adapter = EventAdapter()
    val viewModel by inject<EventViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListeners()
    }

    private fun initAdapter() {
        binding.eventRecycler.adapter = adapter
        viewModel.getData().observe(viewLifecycleOwner, { events ->
            adapter.setData(events)
        })
    }

    private fun initBottomSheetDialog() {
        val bottomDialog = CreateEventBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun initListeners() {
        binding.addEventButton.setOnClickListener {
            initBottomSheetDialog()
        }
        backClick()
    }
}