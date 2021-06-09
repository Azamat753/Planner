package com.lawlett.planner.ui.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.calendarhorizontal.inflate

abstract class SuperBaseAdapter<Data: Any>(protected var dataList: List<Data>) : RecyclerView.Adapter<SuperBaseViewHolder<Data>>() {
    abstract val layoutItemId: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperBaseViewHolder<Data> =
        SuperBaseViewHolder(parent,layoutId = layoutItemId)

    override fun getItemCount(): Int = dataList.size
}

class SuperBaseViewHolder<in T>(parent: ViewGroup, @LayoutRes layoutId: Int)
    : RecyclerView.ViewHolder(parent.inflate(layoutId)) {

}
