package com.lawlett.planner.ui.fragment.timing

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentTimingBinding
import com.lawlett.planner.extensions.explosionView
import com.lawlett.planner.ui.adapter.TimingAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.ChooseTimeBottomSheetDialog
import com.lawlett.planner.ui.dialog.CreateSkillBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.SwipeHelper
import org.koin.android.ext.android.inject

class TimingFragment : BaseFragment<FragmentTimingBinding>(FragmentTimingBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<SkillModel> {

    var listSkill: List<SkillModel>? = null
    private val adapter = TimingAdapter()
    private val viewModel by inject<SkillViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
        swipeItem()
    }

    private fun getData() {
        viewModel.getData().observe(viewLifecycleOwner, { skills ->
            if (skills.isNotEmpty()) {
                listSkill = skills
                adapter.setData(skills)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun openTimerFragmentAndSendModel(model: SkillModel) {
        val pAction: TimingFragmentDirections.ActionTimingFragmentToTimerFragment =
            TimingFragmentDirections.actionTimingFragmentToTimerFragment(model)
        findNavController().navigate(pAction)
    }

    private fun openStopwatchFragmentAndSendModel(model: SkillModel) {
        val pAction: TimingFragmentDirections.ActionTimingFragmentToStopwatchFragment=
            TimingFragmentDirections.actionTimingFragmentToStopwatchFragment(model)
        findNavController().navigate(pAction)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.to_delete),
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    binding.timingRecycler.findViewHolderForAdapterPosition(
                        position
                    )?.itemView?.explosionView(explosionField)

                    viewModel.delete(listSkill?.get(position))
                    if (position == 0) {
                        findNavController().navigate(R.id.timing_fragment)
                    } else {
                        adapter.notifyItemRemoved(position)
                    }
                }
            })
    }

    private fun timerButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.timer),
            14.0f,
            android.R.color.holo_green_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    openTimerFragmentAndSendModel(listSkill!![position])
                }
            })
    }

    private fun stopwatchButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.stopwatch),
            14.0f,
            android.R.color.holo_blue_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    openStopwatchFragmentAndSendModel(listSkill!![position])
                }
            })
    }

    private fun swipeItem() {
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.timingRecycler) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val buttons: List<UnderlayButton>
                val deleteButton = deleteButton(position)
                val timer = timerButton(position)
                val stopwatch = stopwatchButton(position)
                buttons = listOf(deleteButton, timer, stopwatch)
                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.timingRecycler)
    }

    private fun initAdapter() {
        binding.timingRecycler.adapter = adapter
        adapter.listener = this
        getData()
    }

    private fun initClickers() {
        binding.skillFab.setOnClickListener { showCreateSkillDialog() }
        binding.timerFab.setOnClickListener { findNavController().navigate(R.id.timerFragment) }
        binding.stopwatchFab.setOnClickListener { findNavController().navigate(R.id.stopwatchFragment) }
    }

    private fun showCreateSkillDialog() {
        val bottomDialog = CreateSkillBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    override fun onClick(model: SkillModel,position:Int) {
        val bottomDialog = ChooseTimeBottomSheetDialog()
        val bundle = Bundle()
        bundle.putSerializable(Constants.SKILL_MODEL, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }
}
