package com.lawlett.planner.ui.timing

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.viewmodels.TimingViewModel
import com.lawlett.planner.databinding.FragmentTimingBinding
import com.lawlett.planner.extensions.invisible
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.adapter.TimingAdapter
import com.lawlett.planner.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class TimingFragment : BaseFragment<FragmentTimingBinding>(FragmentTimingBinding::inflate) {

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
    private val adapter = TimingAdapter()
    private val viewModel by inject<TimingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initClickers()
        checkRecordsOnEmpty()
        getData()

    }

    private fun getData() {
//        viewModel.getData().observe(viewLifecycleOwner, Observer { timings ->
//            adapter.setData(timings)
//        })

    }

    private fun initRecycler() {
        binding.timingRecycler.adapter = adapter
    }

    private fun checkRecordsOnEmpty() {

    }

    private fun initClickers() {
        binding.fab.setOnClickListener { onAddButtonClicked() }
        binding.stopwatchFab.setOnClickListener { findNavController().navigate(R.id.action_timing_fragment_to_stopwatchFragment) }
        binding.timerFab.setOnClickListener { findNavController().navigate(R.id.action_timing_fragment_to_timerFragment) }
    }

    private fun onAddButtonClicked() {
        setVisibility(isClicked)
        setAnimation(isClicked)
        isClicked = !isClicked
    }

    private fun setAnimation(isClicked: Boolean) {
        if (!isClicked) {
            binding.fab2Container.visible();
            binding.fab3Container.visible()
        }
        binding.fab2Container.invisible();
        binding.fab3Container.invisible()
    }

    private fun setVisibility(isClicked: Boolean) {
        if (!isClicked) {
            binding.fab2Container.startAnimation(fromBottom)
            binding.fab3Container.startAnimation(fromBottom)
            binding.fab.startAnimation(rotateOpen)
        } else {
            binding.fab2Container.startAnimation(toBottom)
            binding.fab3Container.startAnimation(toBottom)
            binding.fab.startAnimation(rotateClose)
        }
    }
}
