package com.lawlett.planner.ui.fragment.standup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.databinding.FragmentCreateStandUpMainBinding
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants


class MainCreateStandUpFragment :
    BaseFragment<FragmentCreateStandUpMainBinding>(FragmentCreateStandUpMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instance = this
        initAdapter()
    }

    companion object {
        lateinit var instance: MainCreateStandUpFragment
    }

    fun getModel(): StandUpModel {
        return arguments?.getSerializable(Constants.UPDATE_MODEL) as StandUpModel
    }

    private fun initAdapter() {
        val adapter = StandUpPagerAdapter(childFragmentManager)
        binding.pagerStandUp.adapter = adapter
        binding.pagerStandUp.setSwipePagingEnabled(false)
        binding.dotsIndicatorStandUp.setViewPager(binding.pagerStandUp)
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