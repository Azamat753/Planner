package com.lawlett.planner.ui.adapter

import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.databinding.ItemStandupBinding
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseAdapter

class StandUpAdapter : BaseAdapter<StandUpModel, ItemStandupBinding>(
    R.layout.item_standup,
    listOf(),
    ItemStandupBinding::inflate
) {
    override fun onBind(binding: ItemStandupBinding, model: StandUpModel) {
        binding.whatDoneYesterdayDesc.text = model.whatDone
        binding.whatPlanDesc.text = model.whatPlan
        binding.prolemsDesc.text = model.problems
        binding.dateCreated.text = model.dateCreated
        if (model.information == null) {
            binding.importantInfoTitle.gone()
        } else {
            binding.importantInfoTitle.visible()
            binding.importantInfoDesc.text = model.information
        }
    }
}