package com.lawlett.planner.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.utils.BoardPreference


class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            var isShown: Boolean = BoardPreference.getInstance(requireContext())!!.isShown
            if (isShown) {
                findNavController().navigate(R.id.action_splash_fragment_to_progress_fragment)
            } else {
                findNavController().navigate(R.id.action_splash_fragment_to_board_fragment)
            }
        }, 2000)
    }




}