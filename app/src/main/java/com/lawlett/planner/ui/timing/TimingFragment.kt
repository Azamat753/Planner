package com.lawlett.planner.ui.timing

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentTimingBinding
import com.lawlett.planner.extensions.invisible
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.adapter.TimingAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.ChooseTimeBottomSheetDialog
import com.lawlett.planner.ui.dialog.fragment.CreateSkillBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class TimingFragment : BaseFragment<FragmentTimingBinding>(FragmentTimingBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<SkillModel> {
    lateinit var touchHelper: ItemTouchHelper

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

    var listSkill: List<SkillModel>? = null
    private var isClicked = false
    private val adapter = TimingAdapter()
    private val viewModel by inject<SkillViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
        swipe()
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

    private fun swipe() {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                 viewModel.delete(listSkill?.get(viewHolder.adapterPosition))
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.timingRecycler)
    }

    private fun initAdapter() {
        binding.timingRecycler.adapter = adapter
        adapter.listener = this
        getData()
    }

    private fun initClickers() {
        binding.fab.setOnClickListener { onAddButtonClicked() }
        binding.stopwatchFab.setOnClickListener { findNavController().navigate(R.id.action_timing_fragment_to_stopwatchFragment) }
        binding.timerFab.setOnClickListener { findNavController().navigate(R.id.action_timing_fragment_to_timerFragment) }
        binding.skillFab.setOnClickListener { showCreateSkillDialog() }
    }

    private fun showCreateSkillDialog() {
        val bottomDialog = CreateSkillBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
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
            binding.fab4Container.visibility
        }
        binding.fab2Container.invisible();
        binding.fab3Container.invisible()
        binding.fab4Container.invisible()
    }

    private fun setVisibility(isClicked: Boolean) {
        if (!isClicked) {
            binding.fab2Container.startAnimation(fromBottom)
            binding.fab3Container.startAnimation(fromBottom)
            binding.fab4Container.startAnimation(fromBottom)
            binding.fab.startAnimation(rotateOpen)
        } else {
            binding.fab2Container.startAnimation(toBottom)
            binding.fab3Container.startAnimation(toBottom)
            binding.fab4Container.startAnimation(toBottom)
            binding.fab.startAnimation(rotateClose)
        }
    }

    override fun onClick(model: SkillModel) {
        val bottomDialog = ChooseTimeBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
        val bundle = Bundle()
        bundle.putSerializable(Constants.SKILL_MODEL, model)
        bottomDialog.arguments = bundle
    }
}
