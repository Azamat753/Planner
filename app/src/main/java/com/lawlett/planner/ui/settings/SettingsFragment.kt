package com.lawlett.planner.ui.settings

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.ThemeModel
import com.lawlett.planner.databinding.FragmentSettingsBinding
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.extensions.showToast
import com.lawlett.planner.ui.adapter.ThemeAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.main.MainActivity
import com.lawlett.planner.utils.BaseAdapter
import com.lawlett.planner.utils.ThemePreference


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<ThemeModel> {

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
//        back_view_settings.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    private fun showSetThemeDialog() {
        val adapter = ThemeAdapter()
        adapter.listener=this
        val alertDialog = Dialog(requireActivity())
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(R.layout.theme_layout)
        val recycler: RecyclerView = alertDialog.findViewById(R.id.theme_recycler)
        recycler.adapter = adapter
        adapter.setData(fillThemeModel())
        alertDialog.show()
    }

    private fun fillThemeModel(): ArrayList<ThemeModel> {
        val listThemeModels: ArrayList<ThemeModel> = ArrayList()
        listThemeModels.add(ThemeModel(getString(R.string.red), "#F44336"))
        listThemeModels.add(ThemeModel(getString(R.string.orange), "#FF5722"))
        listThemeModels.add(ThemeModel(getString(R.string.yellow), "#E8D637"))
        listThemeModels.add(ThemeModel(getString(R.string.blue), "#0365C4"))
        listThemeModels.add(ThemeModel(getString(R.string.black), "#000000"))
        listThemeModels.add(ThemeModel(getString(R.string.green), "#8BC34A"))
        listThemeModels.add(ThemeModel(getString(R.string.white), "#8BC34A"))
        return listThemeModels
    }

    private fun changeTheme() {
        binding.changeThemeButton.setOnClickListener { showSetThemeDialog() }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        startActivity(intent)
    }

    override fun onClick(model: ThemeModel) {
        ThemePreference.getInstance(requireContext())?.saveTheme(model.colorText)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().overridePendingTransition(
                android.R.anim.fade_in, android.R.anim.fade_out
            )
    }
}