package com.lawlett.planner.ui.fragment.focus

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentFocusBinding
import com.lawlett.planner.extensions.explosionView
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.adapter.FocusAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.ChooseTimeBottomSheetDialog
import com.lawlett.planner.ui.dialog.CreateSkillBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class FocusFragment : BaseFragment<FragmentFocusBinding>(FragmentFocusBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<SkillModel>,
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<SkillModel> {

    var listSkill: List<SkillModel>? = null
    private val adapter = FocusAdapter()
    private val viewModel by inject<SkillViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
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
        val pAction: FocusFragmentDirections.ActionTimingFragmentToTimerFragment =
            FocusFragmentDirections.actionTimingFragmentToTimerFragment(model)
        findNavController().navigate(pAction)
    }

    private fun openStopwatchFragmentAndSendModel(model: SkillModel) {
        val pAction: FocusFragmentDirections.ActionTimingFragmentToStopwatchFragment =
            FocusFragmentDirections.actionTimingFragmentToStopwatchFragment(model)
        findNavController().navigate(pAction)
    }

    private fun initAdapter() {
        binding.timingRecycler.adapter = adapter
        adapter.listener = this
        adapter.longListenerWithModel=this
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

    override fun onClick(model: SkillModel, position: Int) {
        val bottomDialog = ChooseTimeBottomSheetDialog()
        val bundle = Bundle()
        bundle.putSerializable(Constants.SKILL_MODEL, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    override fun onLongClick(model: SkillModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete: Button = dialog.findViewById(R.id.delete_button)
        val timer: Button = dialog.findViewById(R.id.edit_button)
        val stopwatch: Button = dialog.findViewById(R.id.third_button)
        timer.text = getString(R.string.timer)
        stopwatch.text = getString(R.string.stopwatch)
        stopwatch.visible()
        delete.setOnClickListener {
            deleteItem(itemView, position)
            dialog.dismiss()
        }
        timer.setOnClickListener {
            openTimerFragmentAndSendModel(model)
            dialog.dismiss()
        }
        stopwatch.setOnClickListener {
            openStopwatchFragmentAndSendModel(model)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteItem(itemView: View, position: Int) {
        itemView.explosionView(explosionField)
        viewModel.delete(listSkill?.get(position))
        if (position == 0) {
            findNavController().navigate(R.id.timing_fragment)
        } else {
            adapter.notifyItemRemoved(position)
        }
    }
}
