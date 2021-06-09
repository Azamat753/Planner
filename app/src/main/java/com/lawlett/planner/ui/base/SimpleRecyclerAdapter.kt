package com.lawlett.planner.ui.base

import androidx.annotation.LayoutRes

class SimpleRecyclerAdapter<RecyclerData: Any>(
    data: List<RecyclerData>,
    @LayoutRes layoutId: Int,
    private val onBindView: SuperBaseViewHolder<RecyclerData>.(data: RecyclerData) -> Unit

): SuperBaseAdapter<RecyclerData>(data) {

    override val layoutItemId: Int = layoutId

    override fun onBindViewHolder(holder: SuperBaseViewHolder<RecyclerData>, position: Int) {
        holder.onBindView(dataList[position])
    }
}