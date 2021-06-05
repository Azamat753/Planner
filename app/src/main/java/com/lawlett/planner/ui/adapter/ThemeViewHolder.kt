package com.lawlett.planner.ui.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.ThemeModel
import com.lawlett.planner.utils.BaseAdapter

class ThemeAdapter : BaseAdapter<ThemeModel>(R.layout.theme_item) {

    override fun onBind(view: View, model: ThemeModel) {
        val themeBackground: LinearLayout = view.findViewById(R.id.theme_background)
        val themeColorTv: TextView = view.findViewById(R.id.theme_color_tv)

        themeBackground.setBackgroundColor(model.colorAttr)
        themeColorTv.text = model.colorText
    }
}