package com.lawlett.planner.ui.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.databinding.FragmentSettingsBinding
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.ChooseThemeBottomSheetDialog


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backClick()
        changeTheme()
        changeLanguage()
    }

    private fun changeLanguage() {
        binding.changeLanguageLayout.setOnClickListener {
            requireActivity().changeLanguage()
        }
    }

    private fun backClick() {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigateUp()
        }
    }

    private fun changeTheme() {
        binding.changeThemeButton.setOnClickListener { initBottomSheet() }
    }

    private fun initBottomSheet() {
        val bottomDialog = ChooseThemeBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        startActivity(intent)
    }
}