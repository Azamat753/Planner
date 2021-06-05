package com.lawlett.planner.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<Binding : ViewBinding, T, ViewHolder: BaseViewHolder<Binding, T>>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> Binding, private val viewHolder: ViewHolder) :
    RecyclerView.Adapter<ViewHolder>() {

    var list: ArrayList<T> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }
}

private interface BaseViewModelBuilder<T> {
    fun onBind(model: T)
}

abstract class BaseViewHolder<Binding : ViewBinding, T>(itemView: Binding) : RecyclerView.ViewHolder(itemView.root), BaseViewModelBuilder<T>