package com.lawlett.planner.ui.onboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.afollestad.viewpagerdots.DotsIndicator
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.databinding.FragmentOnboardBinding


class OnBoardFragment : BaseFragment<FragmentOnboardBinding>(FragmentOnboardBinding::inflate) {
    private val viewPager: ViewPager? = null
    private val dots: DotsIndicator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager?.adapter= fragmentManager?.let { HomePagerAdapter(it)}
        binding.pager.adapter = HomePagerAdapter(childFragmentManager)
        dots?.attachViewPager(viewPager)

    }

    class HomePagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            val fragment: Fragment = BoardFragment()
            val bundle = Bundle()
            bundle.putInt("pos",position)
            fragment.arguments=bundle
            return fragment
        }
        override fun getCount(): Int {
            return 4
        }
    }
}