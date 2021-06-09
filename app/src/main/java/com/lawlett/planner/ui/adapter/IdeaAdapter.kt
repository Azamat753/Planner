package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.databinding.IdeaItemBinding
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.utils.BaseAdapter

class IdeaAdapter : BaseAdapter<IdeaModel, IdeaItemBinding>(
    R.layout.idea_item,
    listOf(),
    inflater = IdeaItemBinding::inflate
) {
    override fun onBind(binding: IdeaItemBinding, model: IdeaModel) {
        binding.imageTitle.loadImage(model.image)
        binding.titleIdea.text = model.title
        binding.lineLeftView.setBackgroundColor(model.color)
    }
}