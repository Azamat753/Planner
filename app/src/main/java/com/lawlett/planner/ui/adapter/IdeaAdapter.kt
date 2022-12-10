package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.databinding.ItemIdeaBinding
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.ui.base.BaseAdapter

class IdeaAdapter : BaseAdapter<IdeaModel, ItemIdeaBinding>(
    R.layout.item_idea,
    listOf(),
    inflater = ItemIdeaBinding::inflate
) {
    override fun onBind(binding: ItemIdeaBinding, model: IdeaModel) {
        binding.imageTitle.loadImage(model.image)
        binding.titleIdea.text = model.title
        binding.lineLeftView.setBackgroundColor(model.color)
    }
}