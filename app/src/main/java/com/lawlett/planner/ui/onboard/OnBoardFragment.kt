package com.lawlett.planner.ui.onboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lawlett.planner.databinding.FragmentOnboardBinding
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants


class OnBoardFragment : BaseFragment<FragmentOnboardBinding>(FragmentOnboardBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = fragmentManager?.let { HomePagerAdapter(it) }
        binding.pagerBoard.adapter = adapter
        binding.dotsIndicatorBoard.setViewPager(binding.pagerBoard)
    }

    class HomePagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            val fragment: Fragment = BoardFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.BOARD_POS, position)
            fragment.arguments = bundle
            return fragment
        }

        override fun getCount(): Int {
            return 4
        }
    }
}