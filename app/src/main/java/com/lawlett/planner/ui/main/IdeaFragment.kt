package com.lawlett.planner.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.utils.visible
import kotlinx.android.synthetic.main.fragment_idea.*

class IdeaFragment : BaseFragment(R.layout.fragment_idea) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_quick_btn.setOnClickListener {
            findNavController().navigate(R.id.action_idea_fragment_to_recordIdeaFragment)

        }
    }
}
