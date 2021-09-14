package com.lawlett.planner.ui.standup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.databinding.FragmentCreateStandUpMainBinding
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants


class MainCreateStandUpFragment :
    BaseFragment<FragmentCreateStandUpMainBinding>(FragmentCreateStandUpMainBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("onViewCreated", "onViewCreated: MainCreateStandUpFragment")
        instance = this
        initAdapter()
    }
    companion object {
        lateinit var instance: MainCreateStandUpFragment
    }

    private fun initAdapter() {
        val adapter = StandUpPagerAdapter(childFragmentManager)
        binding.pagerStandUp.adapter = adapter
        binding.dotsIndicatorStandUp.setViewPager(binding.pagerStandUp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onViewCreated", "onCreate: MainCreateStandUpFragment")
    }

    class StandUpPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            val fragment: Fragment = CreateStandUpFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.STAND_UP_POS, position)
            fragment.arguments = bundle
            return fragment
        }

        override fun getCount(): Int {
            return 4
        }
    }
}