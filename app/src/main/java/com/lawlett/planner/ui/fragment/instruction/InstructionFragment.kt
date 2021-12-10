package com.lawlett.planner.ui.fragment.instruction

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.lawlett.planner.R
import com.lawlett.planner.databinding.FragmentInstructionBinding
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseFragment


class InstructionFragment :
    BaseFragment<FragmentInstructionBinding>(FragmentInstructionBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNameForButtons()
        initClickers()
        onBackPressOverride(R.id.settingsFragment)
    }

    private fun setNameForButtons() {
        binding.mainScreenBtn.title.text = getString(R.string.main)
        binding.categoryBtn.title.text = getString(R.string.categories)
        binding.timetableBtn.title.text = getString(R.string.timetable)
        binding.eventBtn.title.text = getString(R.string.events)
        binding.habitBtn.title.text = getString(R.string.habit)
        binding.focusBtn.title.text = getString(R.string.focus)
        binding.standupBtn.title.text = getString(R.string.stand_up)
        binding.dreamBtn.title.text = getString(R.string.dream)
        binding.ideaBtn.title.text = getString(R.string.ideas)
        binding.financeBtn.title.text = getString(R.string.finance)
        binding.settingBtn.title.text = getString(R.string.settings)
    }

    private fun initClickers() {
        binding.mainScreenBtn.titleCard.setOnClickListener { setVisibility(binding.mainScreenTv) }
        binding.categoryBtn.titleCard.setOnClickListener { setVisibility(binding.categoryTv) }
        binding.timetableBtn.titleCard.setOnClickListener { setVisibility(binding.timetableTv) }
        binding.eventBtn.titleCard.setOnClickListener { setVisibility(binding.eventTv) }
        binding.habitBtn.titleCard.setOnClickListener { setVisibility(binding.habitTv) }
        binding.focusBtn.titleCard.setOnClickListener { setVisibility(binding.focusTv) }
        binding.standupBtn.titleCard.setOnClickListener { setVisibility(binding.standupTv) }
        binding.dreamBtn.titleCard.setOnClickListener { setVisibility(binding.dreamTv) }
        binding.ideaBtn.titleCard.setOnClickListener { setVisibility(binding.ideaTv) }
        binding.financeBtn.titleCard.setOnClickListener { setVisibility(binding.financeTv) }
        binding.settingBtn.titleCard.setOnClickListener { setVisibility(binding.settingTv) }
    }

    private fun setVisibility(textView: TextView) {
        if (textView.isVisible) {
            textView.gone()
        } else {
            textView.visible()
        }
    }
}