package com.lawlett.planner.ui.standup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.lawlett.planner.R
import com.lawlett.planner.databinding.FragmentCreateStandUpMainBinding
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.invisible
import com.lawlett.planner.extensions.showToast
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants
import java.lang.Exception


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

    init {
        fun getInstance(): MainCreateStandUpFragment {
            return instance
        }
    }

    private fun initAdapter() {
        val adapter = fragmentManager?.let { StandUpPagerAdapter(it) }
        binding.pagerStandUp.adapter = adapter
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