package com.lawlett.planner.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.databinding.FragmentIdeaBinding
import kotlinx.android.synthetic.main.fragment_idea.*

class IdeaFragment : BaseFragment<FragmentIdeaBinding>(FragmentIdeaBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_quick_btn.setOnClickListener {
            findNavController().navigate(R.id.action_idea_fragment_to_recordIdeaFragment)

        }
    }
}
