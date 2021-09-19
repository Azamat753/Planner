package com.lawlett.planner.ui.onboard

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.databinding.FragmentBoardBinding
import com.lawlett.planner.utils.BoardPreference
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.utils.Constants

class BoardFragment : BaseFragment<FragmentBoardBinding>(FragmentBoardBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        setView()
    }

    private fun setView() {
        when (arguments?.getInt(Constants.BOARD_POS)) {
            0 -> {
                binding.titleTv.setText(R.string.event_calendar)
                binding.descTv.setText(R.string.fast_add_your_event)
                binding.calendarAnimation.visible()
                binding.changeLang.visible()
            }
            1 -> {
                binding.titleTv.setText(R.string.done_tasks)
                binding.descTv.setText(R.string.list_tasks_help_you)
                binding.todoAnimation.visible()
            }
            2 -> {
                binding.titleTv.setText(R.string.record_idea_title)
                binding.descTv.setText(R.string.most_effect_idea)
                binding.notesAnimation.visible()
            }
            3 -> {
                binding.titleTv.setText(R.string.check_timing)
                binding.descTv.setText(R.string.plus_you_kpd)
                binding.timeAnimation.visible()
                binding.startTv.visible()
            }

        }
    }

    private fun initClickers() {
        binding.changeLang.setOnClickListener {
//            requireActivity().changeLanguage()
        }
        binding.startTv.setOnClickListener {
            BoardPreference.getInstance(requireContext())!!.saveShown()
            findNavController().navigate(R.id.action_board_fragment_to_progress_fragment)
        }
    }
}