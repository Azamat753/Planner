package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.databinding.ItemCategoryBinding
import com.lawlett.planner.databinding.ItemCategoryNewBinding
import com.lawlett.planner.ui.base.BaseAdapter

class CategoryAdapter : BaseAdapter<CategoryModel, ItemCategoryBinding>(
    R.layout.item_category,
    listOf(),
    ItemCategoryBinding::inflate
) {
    override fun onBind(binding: ItemCategoryBinding, model: CategoryModel) {
        model.categoryIcon?.let { binding.categoryImage.text =(it) }
        binding.categoryName.text = model.categoryName
        binding.taskAmount.text = model.taskAmount.toString()
    }
}