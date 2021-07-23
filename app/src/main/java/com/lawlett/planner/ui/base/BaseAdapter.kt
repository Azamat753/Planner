package com.lawlett.planner.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, Binding : ViewBinding>(
    private val holderLayoutId: Int,
    var data: List<T>,
    private val inflater: (LayoutInflater) -> Binding
) : RecyclerView.Adapter<BaseAdapter<T, Binding>.BaseViewHolder>() {

    var listener: IBaseAdapterClickListener<T>? = null
    private var _binding: Binding? = null
    val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        _binding = inflater.invoke(LayoutInflater.from(parent.context))
        return BaseViewHolder(binding)
    }

    @JvmName("setData1")
    fun setData(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    abstract fun onBind(binding: Binding, model: T)

    inner class BaseViewHolder(binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: T) {
            onBind(binding, model)
            itemView.setOnClickListener { listener?.onClick(model) }
        }
    }

    interface IBaseAdapterClickListener<T> {
        fun onClick(model: T)
    }
}