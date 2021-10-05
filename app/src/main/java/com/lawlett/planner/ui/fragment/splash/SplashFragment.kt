package com.lawlett.planner.ui.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.databinding.FragmentSplashBinding
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants


class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            val isShown: Boolean = BooleanPreference.getInstance(requireContext())?.getBooleanData(Constants.SPLASH_SCREEN)!!
            if (isShown) {
                findNavController().navigate(R.id.progress_fragment)
            } else {
                findNavController().navigate(R.id.introFragment)
            }
        }, 2000)
    }
}