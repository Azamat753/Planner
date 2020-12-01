package com.lawlett.planner.ui.main.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.utils.ThemePreference
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.extensions.loadLocale
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

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
            when (ThemePreference.getInstance(requireContext())?.getLightTheme) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    ThemePreference.getInstance(requireContext())?.saveLightFalse()
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    ThemePreference.getInstance(requireContext())?.saveLightTrue()
                }
            }
        }
    }
}