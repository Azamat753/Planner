package com.lawlett.planner.ui.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.databinding.FragmentSplashBinding
import com.lawlett.planner.ui.base.BaseFragment


class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
//            var isShown: Boolean = BoardPreference.getInstance(requireContext())!!.isShown
//            if (isShown) {
                findNavController().navigate(R.id.progress_fragment)
//            } else {
//                findNavController().navigate(R.id.action_splash_fragment_to_board_fragment)
//            }
        }, 2000)
    }
}