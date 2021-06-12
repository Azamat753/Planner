package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.databinding.ItemIdeaProgressBinding
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.ui.base.BaseAdapter

class IdeaProgressAdapter :
    BaseAdapter<IdeaModel, ItemIdeaProgressBinding>(
        R.layout.item_idea,
        listOf(),
        ItemIdeaProgressBinding::inflate
    ) {
    override fun onBind(binding: ItemIdeaProgressBinding, model: IdeaModel) {
        binding.titleIdea.text = model.title
        binding.imageTitle.loadImage(model.image)
    }
}