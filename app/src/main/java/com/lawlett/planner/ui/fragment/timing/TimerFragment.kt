package com.lawlett.planner.ui.fragment.timing

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentTimerBinding
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.showToast
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.SimpleCountDownTimer
import org.koin.android.ext.android.inject
import java.lang.reflect.InvocationTargetException

class TimerFragment : BaseFragment<FragmentTimerBinding>(FragmentTimerBinding::inflate) {
    private var timeLeftInMilliseconds: Long = 0
    private lateinit var notificationManager: NotificationManagerCompat
    var myTask: String? = null
    var timeLeftText: String? = null
    private var atg: Animation? = null
    private var btgOne: Animation? = null
    private var btgTwo: Animation? = null
    private val viewModel by inject<SkillViewModel>()
    private val args: TimerFragmentArgs by navArgs()
    private var modelId: Int = 0
    private var previousTime: Double = 0.0
    private lateinit var previousDateCreated: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        fillFieldsForUpdateTimer()
        initClickers()
        onBackPressOverride(R.id.timing_fragment)
    }

    private fun fillFieldsForUpdateTimer() {
        if (isUpdate()) {
            val model = args.model
            binding.timerTaskEdit.setText(model.skillName)
            modelId = model.id!!
            previousTime = model.hour!!.toDouble()
            previousDateCreated = model.dateCreated.toString()
        }
    }

    private fun isUpdate(): Boolean {
        var isHave = false
        try {
            isHave = args.model.skillName != null
        } catch (inTarEx: InvocationTargetException) {
            inTarEx.printStackTrace()
        } catch (ar: IllegalArgumentException) {
            ar.printStackTrace()
        }
        return isHave
    }

    private fun initAnimation() {
        atg = AnimationUtils.loadAnimation(requireContext(), R.anim.atg)
        btgOne = AnimationUtils.loadAnimation(requireContext(), R.anim.btgone)
        btgTwo = AnimationUtils.loadAnimation(requireContext(), R.anim.btgtwo)
    }

    private fun initClickers() {
        binding.timerTaskApply.setOnClickListener {
            if (binding.timerTaskEdit.text.toString().isEmpty()) {
                requireContext().showToast(getString(R.string.empty))
            } else {
                myTask = binding.timerTaskEdit.text.toString()
                binding.imageConst.gone()
                binding.timerConst.visible()
            }
        }
        binding.applyButton.setOnClickListener {
            if (binding.countTime.text.toString() == "" || binding.countTime.text.toString()
                    .toInt() < 1
            ) {
                requireContext().showToast(getString(R.string.zero_minutes_pass))
            } else {
                timeLeftInMilliseconds =
                    (binding.countTime.text.toString().toInt() * 60000).toLong()
                timeLeftText = timeLeftInMilliseconds.toString()
                binding.skillNameTv.text = myTask
                binding.applyButton.gone()
                binding.countTime.gone()
                binding.countdownText.text = getString(R.string.ready)
                binding.countdownButton.visible()
                binding.countdownText.visible()
                binding.mgAnimation.gone()
                binding.clockAnimation.visible()
                binding.skillNameTv.visible()
            }
        }

        binding.countdownButton.setOnClickListener {
            startTimer()
        }

        binding.exitButton.setOnClickListener {
            if (isUpdate()) {
                val hour = calculateRemainingTime().toDouble() + previousTime
                val model = SkillModel(
                    skillName = myTask,
                    dateCreated = previousDateCreated,
                    id = modelId,
                    hour = hour.toString()
                )
                viewModel.update(model)
            } else {
                val model = SkillModel(
                    hour = calculateRemainingTime(),
                    skillName = myTask,
                    dateCreated = getTodayDate()
                )
                viewModel.insertData(model)
            }
            findNavController().navigate(R.id.timing_fragment)
        }
    }

    private fun calculateRemainingTime(): String {
        val currentTime = timeLeftText?.substringBefore(":")?.toDouble()
        val time = (binding.countTime.text.toString().toDouble() - currentTime!!) / 60
        return time.toString()
    }

    private fun startTimer() {
        binding.countdownButton.animate().alpha(0f).setDuration(300).start()
        object : SimpleCountDownTimer(timeLeftInMilliseconds, 1000) {
            override fun onTick(p0: Long) {
                super.onTick(p0)
                val minutes: Int = p0.toInt() / 60000
                val seconds: Int = p0.toInt() % 60000 / 1000

                timeLeftText = "" + minutes
                timeLeftText += ":"
                if (seconds < 10) timeLeftText += ""
                timeLeftText += seconds
                try {
                    binding.countdownText.text = timeLeftText
                    binding.countdownButton.gone()
                    binding.exitButton.visible()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                super.onFinish()
                binding.countdownButton.visible()
            }
        }.start()
    }
}