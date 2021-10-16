package com.lawlett.planner.ui.fragment.focus

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentFocusBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.FocusAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.ChooseTimeBottomSheetDialog
import com.lawlett.planner.ui.dialog.CreateSkillBottomSheetDialog
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.Target
import org.koin.android.ext.android.inject
import java.util.*

class FocusFragment : BaseFragment<FragmentFocusBinding>(FragmentFocusBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<SkillModel>,
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<SkillModel>, CheckListEvent {

    private var listSize = 0
    private val adapter = FocusAdapter()
    private val viewModel by inject<SkillViewModel>()
    private val achievementViewModel by inject<AchievementViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
        addFalseDataForExample()
        showSpotlight()
        backToProgress()
    }

    private fun showSpotlight() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.FOCUS_INSTRUCTION) == false
        ) {
            val targets = ArrayList<Target>()
            val root = FrameLayout(requireContext())
            val first = layoutInflater.inflate(R.layout.layout_target, root)
            val view = View(requireContext())

            Handler().postDelayed({
                val firstSpot = setSpotLightTarget(
                    view,
                    first,
                    "\n\n\n\n " + getString(R.string.focus) + " \n\n\n " + getString(R.string.list_process) + " \n " + getString(
                        R.string.theroy_thousand_hour
                    ) + " \n" + getString(
                        R.string.profession_thousand_hour
                    )
                )
                val secondSpot = setSpotLightTarget(
                    view,
                    first,
                    getString(R.string.insert_button) + " \n " + getString(R.string.work_stopwatch_timer)
                )
                val thirdSpot = setSpotLightTarget(
                    view,
                    first,
                    getString(R.string.hold_card)
                )
                targets.add(firstSpot)
                targets.add(secondSpot)
                targets.add(thirdSpot)
                setSpotLightBuilder(requireActivity(), targets, first)
            }, 100)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.FOCUS_INSTRUCTION, true)
        }
    }

    private fun addFalseDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.FOCUS_EXAMPLE_DATA) == false
        ) {
            val model = SkillModel(
                skillName = getString(R.string.diplom_work),
                hour = "12.7",
                dateCreated = getTodayDate(requireContext())
            )
            val model2 =
                SkillModel(
                    skillName = getString(R.string.phisology),
                    hour = "46.2",
                    dateCreated = getTodayDate(requireContext())
                )
            viewModel.insertData(model)
            viewModel.insertData(model2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.FOCUS_EXAMPLE_DATA, true)
        }
    }

    private fun getData() {
        viewModel.getData().observe(viewLifecycleOwner, { skills ->
            if (skills.isNotEmpty()) {
                listSize=skills.size
                adapter.setData(skills)
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
        adapter.longListenerWithModel = this
        getData()
    }

    private fun initClickers() {
        binding.skillFab.setOnClickListener { showCreateSkillDialog() }
        binding.timerFab.setOnClickListener { findNavController().navigate(R.id.timerFragment) }
        binding.stopwatchFab.setOnClickListener { findNavController().navigate(R.id.stopwatchFragment) }
    }

    private fun showCreateSkillDialog() {
        val bottomDialog = CreateSkillBottomSheetDialog(this)
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
            deleteItem(model, itemView, position)
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

    private fun deleteItem(model: SkillModel, itemView: View, position: Int) {
        itemView.explosionView(explosionField)
        viewModel.delete(model)
        if (position == 0) {
            findNavController().navigate(R.id.timing_fragment)
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onStop() {
        super.onStop()
        clearAnimations(achievementView = binding.achievementView)
    }

    override fun check() {
        adapter.notifyDataSetChanged()
        rewardAnAchievement(listSize,requireActivity(),achievementViewModel,binding.achievementView)
    }
}
