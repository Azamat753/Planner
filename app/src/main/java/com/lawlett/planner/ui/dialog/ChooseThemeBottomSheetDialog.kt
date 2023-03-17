package com.lawlett.planner.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.ThemeModel
import com.lawlett.planner.databinding.ThemeLayoutBinding
import com.lawlett.planner.ui.activity.MainActivity
import com.lawlett.planner.ui.adapter.ThemeAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.IntPreference

class ChooseThemeBottomSheetDialog :
    BaseBottomSheetDialog<ThemeLayoutBinding>(ThemeLayoutBinding::inflate),BaseAdapter.IBaseAdapterClickListener<ThemeModel> {
    val adapter = ThemeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        adapter.listener=this
        binding.themeRecycler.adapter=adapter
        adapter.setData(fillThemeModel())
    }
    override fun onClick(model: ThemeModel,position:Int) {
        IntPreference.getInstance(requireContext())?.saveInt(Constants.THEME_PREFERENCE,position)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().overridePendingTransition(
            android.R.anim.fade_in, android.R.anim.fade_out
        )
    }

    private fun fillThemeModel(): ArrayList<ThemeModel> {
        val listThemeModels: ArrayList<ThemeModel> = ArrayList()
        listThemeModels.add(ThemeModel(getString(R.string.blue), "#0365C4"))
        listThemeModels.add(ThemeModel(getString(R.string.orange), "#FF5722"))
        listThemeModels.add(ThemeModel(getString(R.string.yellow), "#FFC03D"))
        listThemeModels.add(ThemeModel(getString(R.string.heavenly), "#73AFBA"))
        listThemeModels.add(ThemeModel(getString(R.string.red), "#FF2525"))
        listThemeModels.add(ThemeModel(getString(R.string.green), "#99DE9F"))
        listThemeModels.add(ThemeModel(getString(R.string.pink), "#FC9885"))
        listThemeModels.add(ThemeModel(getString(R.string.black), "#323232"))
        listThemeModels.add(ThemeModel(getString(R.string.bilberry), "#464196"))
        listThemeModels.add(ThemeModel(getString(R.string.violet), "#673AB7"))
        return listThemeModels
    }
}