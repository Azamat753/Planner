package com.lawlett.planner.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.cdev.achievementview.AchievementView
import com.lawlett.planner.R
import nl.dionsegijn.konfetti.KonfettiView
import tyrantgit.explosionfield.ExplosionField

abstract class BaseFragment<T : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> T) :
    Fragment() {
    lateinit var explosionField: ExplosionField

    private var _binding: T? = null
    val binding get() = _binding!!

    //Инициализация через view binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        explosionField = ExplosionField.attach2Window(requireActivity())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun backToProgress() {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.progress_fragment)
        }
    }
    fun onBackPressOverride(id:Int) {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(id)
        }
    }
    fun clearAnimations(konfettiView: KonfettiView?=null,achievementView: AchievementView){
        konfettiView?.clearAnimation()
        achievementView.clearAnimation()
    }
}
