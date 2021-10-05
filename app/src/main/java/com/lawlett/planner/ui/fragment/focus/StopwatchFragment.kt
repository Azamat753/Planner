package com.lawlett.planner.ui.fragment.focus

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentStopwatchBinding
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseFragment
import org.koin.android.ext.android.inject
import java.lang.reflect.InvocationTargetException

class StopwatchFragment :
    BaseFragment<FragmentStopwatchBinding>(FragmentStopwatchBinding::inflate) {
    var elapsedMillis: Long = 0
    private var myTask: String? = null
    var stopwatchTime: String? = null
    private var notificationManager: NotificationManagerCompat? = null
    private val viewModel by inject<SkillViewModel>()
    private val args: TimerFragmentArgs by navArgs()
    private var modelId: Int = 0
    private var previousTime: Double = 0.0
    private lateinit var previousDateCreated: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationManager = NotificationManagerCompat.from(requireContext())
        fillFieldsForUpdateStopwatch()
        initListeners()
        onBackPressOverride(R.id.timing_fragment)
    }

    private fun fillFieldsForUpdateStopwatch() {
        if (isUpdate()) {
            val model = args.model
            binding.stopwatchTaskEdit.setText(model.skillName)
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

    private fun initListeners() {
        binding.stopwatchTaskApply.setOnClickListener {
            myTask = binding.stopwatchTaskEdit.text.toString().trim()
            binding.imageconst.gone()
            binding.stopWatchConst.visible()
            binding.skillNameTv.visible()
            binding.skillNameTv.text = myTask
        }
        binding.btnstart.setOnClickListener {
            binding.btnstop.animate().alpha(1f).translationY(-80f).setDuration(300).start()
            binding.btnstart.animate().alpha(0f).setDuration(300).start()
            binding.btnstop.visible()
            binding.btnstart.gone()
            binding.timerHere.base = SystemClock.elapsedRealtime()
            binding.timerHere.start()
        }
        binding.btnstop.setOnClickListener {
            if (isUpdate()) {
                val hour = showElapsedTime().toDouble() + previousTime
                val model = SkillModel(
                    hour = hour.toString(),
                    dateCreated = previousDateCreated,
                    id = modelId,
                    skillName = myTask
                )
                viewModel.update(model)
            } else {
                val skillModel = SkillModel(
                    hour = showElapsedTime(),
                    dateCreated = getTodayDate(requireContext()),
                    skillName = myTask
                )
                viewModel.insertData(skillModel)
            }
            findNavController().navigate(R.id.timing_fragment)
        }
    }

    private fun showElapsedTime(): String {
        elapsedMillis = SystemClock.elapsedRealtime() - binding.timerHere.base
        stopwatchTime = ((elapsedMillis / 60000).toDouble() / 60).toString()
        return stopwatchTime.toString()
    }

}