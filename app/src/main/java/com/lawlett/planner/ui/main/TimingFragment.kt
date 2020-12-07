package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.data.room.viewmodels.TimingViewModel
import com.lawlett.planner.extensions.invisible
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.adapter.TimingAdapter
import kotlinx.android.synthetic.main.fragment_timing.*

class TimingFragment : BaseFragment(R.layout.fragment_timing) {

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }
    private var isClicked = false
    private val timingAdapter=TimingAdapter()
    private lateinit var mTimingViewModel: TimingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initClickers()
        checkRecordsOnEmpty()

    }

    private fun initRecycler() {
        timing_recycler.adapter=timingAdapter
    }

    private fun checkRecordsOnEmpty() {
        
    }

    private fun initClickers() {
        fab.setOnClickListener { onAddButtonClicked() }
        stopwatch_fab.setOnClickListener { findNavController().navigate(R.id.action_timing_fragment_to_stopwatchFragment)}
        timer_fab.setOnClickListener { findNavController().navigate(R.id.action_timing_fragment_to_timerFragment) }
    }
    private fun onAddButtonClicked() {
        setVisibility(isClicked)
        setAnimation(isClicked)
        isClicked = !isClicked
    }
    private fun setAnimation(isClicked: Boolean) {
        if (!isClicked) {
            fab2_container.visible();
            fab3_container.visible()
        }
        fab2_container.invisible();
        fab3_container.invisible()
    }
    private fun setVisibility(isClicked: Boolean) {
        if (!isClicked) {
            fab2_container.startAnimation(fromBottom)
            fab3_container.startAnimation(fromBottom)
            fab.startAnimation(rotateOpen)
        } else {
            fab2_container.startAnimation(toBottom)
            fab3_container.startAnimation(toBottom)
            fab.startAnimation(rotateClose)
        }
    }
}
