package com.lawlett.planner.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.LanguageModel
import com.lawlett.planner.databinding.FragmentSettingsBinding
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.getLanguageList
import com.lawlett.planner.ui.adapter.LanguageAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.ChooseThemeBottomSheetDialog


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate),BaseAdapter.IBaseAdapterClickListener<LanguageModel> {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backClick()
        changeTheme()
        showLanguageDialog()
    }

    private fun languageAdapter() {
        val adapter = LanguageAdapter()
        adapter.listener= this
        adapter.setData(getLanguageList())
        val dialog = requireContext().getDialog(R.layout.language_dialog)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.history_recycler)
        recyclerView.adapter = adapter
        dialog.show()
    }

    fun showLanguageDialog() {
        binding.changeLanguageLayout.setOnClickListener { languageAdapter() }
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

    override fun onClick(model: LanguageModel, position: Int) {
        requireActivity().changeLanguage(position)
    }
}