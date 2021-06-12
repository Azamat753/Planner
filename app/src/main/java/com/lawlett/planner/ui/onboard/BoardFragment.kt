package com.lawlett.planner.ui.onboard

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.databinding.FragmentBoardBinding
import com.lawlett.planner.utils.BoardPreference
import com.lawlett.planner.extensions.changeLanguage

class BoardFragment : BaseFragment<FragmentBoardBinding>(FragmentBoardBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeLang.setOnClickListener {
            requireActivity().changeLanguage()
        }

        when (arguments?.getInt("pos")) {
            0 -> {
                binding.titleTv.setText(R.string.event_calendar)
                binding.descTv.setText(R.string.fast_add_your_event)
                binding.calendarAnimation.visibility = View.VISIBLE
                binding.todoAnimation.visibility = View.GONE
                binding.notesAnimation.visibility = View.GONE
                binding.changeLang.visibility = View.VISIBLE
            }
            1 -> {
                binding.titleTv.setText(R.string.done_tasks)
                binding.descTv.setText(R.string.list_tasks_help_you)
                binding.calendarAnimation.visibility = View.GONE
                binding.todoAnimation.visibility = View.VISIBLE
                binding.notesAnimation.visibility = View.GONE
                binding.changeLang.visibility = View.GONE
            }
            2 -> {
                binding.titleTv.setText(R.string.record_idea_title)
                binding.descTv.setText(R.string.most_effect_idea)
                binding.calendarAnimation.visibility = View.GONE
                binding.todoAnimation.visibility = View.GONE
                binding.notesAnimation.visibility = View.VISIBLE
                binding.changeLang.visibility = View.GONE
            }
            3 -> {
                binding.titleTv.setText(R.string.check_timing)
                binding.descTv.setText(R.string.plus_you_kpd)
                binding.calendarAnimation.visibility = View.GONE
                binding.todoAnimation.visibility = View.GONE
                binding.notesAnimation.visibility = View.GONE
                binding.timeAnimation.visibility = View.VISIBLE
                binding.startTv.visibility = View.VISIBLE
                binding.changeLang.visibility = View.GONE
            }

        }
        binding.startTv.setOnClickListener {
            BoardPreference.getInstance(requireContext())!!.saveShown()
            findNavController().navigate(R.id.action_board_fragment_to_progress_fragment)
        }
    }
}