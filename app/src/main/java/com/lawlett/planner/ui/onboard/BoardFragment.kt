package com.lawlett.planner.ui.onboard

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.utils.BoardPreference
import com.lawlett.planner.extensions.changeLanguage
import kotlinx.android.synthetic.main.fragment_board.*

class BoardFragment : BaseFragment(R.layout.fragment_board) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        change_lang.setOnClickListener {
            requireActivity().changeLanguage()
        }

        when (arguments?.getInt("pos")) {
            0 -> {
                title_tv.setText(R.string.event_calendar)
                desc_tv.setText(R.string.fast_add_your_event)
                calendar_animation.visibility = View.VISIBLE
                todo_animation.visibility = View.GONE
                notes_animation.visibility = View.GONE
                change_lang.visibility = View.VISIBLE
            }
            1 -> {
                title_tv.setText(R.string.done_tasks)
                desc_tv.setText(R.string.list_tasks_help_you)
                calendar_animation.visibility = View.GONE
                todo_animation.visibility = View.VISIBLE
                notes_animation.visibility = View.GONE
                change_lang.visibility = View.GONE
            }
            2 -> {
                title_tv.setText(R.string.record_idea_simple)
                desc_tv.setText(R.string.most_effect_idea)
                calendar_animation.visibility = View.GONE
                todo_animation.visibility = View.GONE
                notes_animation.visibility = View.VISIBLE
                change_lang.visibility = View.GONE
            }
            3 -> {
                title_tv.setText(R.string.check_timing)
                desc_tv.setText(R.string.plus_you_kpd)
                calendar_animation.visibility = View.GONE
                todo_animation.visibility = View.GONE
                notes_animation.visibility = View.GONE
                time_animation.visibility = View.VISIBLE
                start_tv.visibility = View.VISIBLE
                change_lang.visibility = View.GONE
            }

        }
        start_tv.setOnClickListener {
            BoardPreference.getInstance(requireContext())!!.saveShown()
            findNavController().navigate(R.id.action_board_fragment_to_progress_fragment)
        }
    }

}