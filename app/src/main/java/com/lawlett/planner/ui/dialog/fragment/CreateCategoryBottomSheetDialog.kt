package com.lawlett.planner.ui.dialog.fragment

import android.os.Bundle
import android.view.View
import com.lawlett.planner.databinding.CreateCategoryBottomSheetDialogBinding
import com.lawlett.planner.ui.base.BaseBottomSheetDialog

class CreateCategoryBottomSheetDialog :
    BaseBottomSheetDialog<CreateCategoryBottomSheetDialogBinding>(
        CreateCategoryBottomSheetDialogBinding::inflate
    ) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}