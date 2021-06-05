package com.lawlett.planner.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.databinding.FragmentSettingsBinding
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.main.MainActivity
import com.lawlett.planner.utils.ThemePreference
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backClick()
        changeTheme()
        changeLanguage()
    }

    private fun changeLanguage() {
        four_layout.setOnClickListener {
            requireActivity().changeLanguage()
        }
    }

    private fun backClick() {
        back_view_settings.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun changeTheme() {
        third_layout.setOnClickListener {
            ThemePreference.getInstance(requireContext())?.saveTheme(1)
            startActivity(Intent(requireContext(),MainActivity::class.java))
            requireActivity().overridePendingTransition(
                android.R.anim.fade_in, android.R.anim.fade_out
            )
        }
    }
}