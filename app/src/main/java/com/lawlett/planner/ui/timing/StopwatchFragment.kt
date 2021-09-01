package com.lawlett.planner.ui.timing

import android.app.Notification
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentStopwatchBinding
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class StopwatchFragment :
    BaseFragment<FragmentStopwatchBinding>(FragmentStopwatchBinding::inflate) {
    var elapsedMillis: Long = 0
    var myTask: String? = null
    var stopwatchTime: String? = null
    private var notificationManager: NotificationManagerCompat? = null
    private val viewModel by inject<SkillViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationManager = NotificationManagerCompat.from(requireContext())
        initAnimation()
        initListeners()
    }

    private fun initListeners() {
        binding.stopwatchTaskApply.setOnClickListener {
            myTask = binding.stopwatchTaskEdit.text.toString()
            binding.imageconst.gone()
            binding.stopWatchConst.visible()
        }
        binding.btnstart.setOnClickListener {
            showCustomNotification()
            binding.btnstop.animate().alpha(1f).translationY(-80f).setDuration(300).start()
            binding.btnstart.animate().alpha(0f).setDuration(300).start()
            binding.btnstop.visible()
            binding.btnstart.gone()
            binding.timerHere.base = SystemClock.elapsedRealtime()
            binding.timerHere.start()
        }
        binding.btnstop.setOnClickListener {
            notificationManager?.cancel(1)

            val skillModel = SkillModel(
                hour = showElapsedTime(),
                dateCreated = getTodayDate(),
                skillName = myTask
            )
            viewModel.insertData(skillModel)
            findNavController().navigate(R.id.timing_fragment)
        }
    }

    private fun showElapsedTime(): String {
        elapsedMillis = SystemClock.elapsedRealtime() - binding.timerHere.base
        stopwatchTime = ((elapsedMillis / 60000).toDouble() / 60).toString()
        return stopwatchTime.toString()
    }

    private fun showCustomNotification() {
        val expandedView = RemoteViews(
            requireContext().packageName,
            R.layout.notification_expanded_stopwatch
        )
        expandedView.setChronometer(
            R.id.timerHere_expanded,
            SystemClock.elapsedRealtime(),
            null,
            true
        )
        val notification = NotificationCompat.Builder(requireContext(), Constants.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_planner)
            .setCustomBigContentView(expandedView)
            .setContentTitle(getString(R.string.stopwatch))
            .setContentText(getString(R.string.go_count))
            .setColor(getColor(requireContext(), R.color.textColor))
            .build()

        notification.flags = Notification.FLAG_ONGOING_EVENT
        notificationManager!!.notify(1, notification)

    }

    private fun initAnimation() {
        val atg = AnimationUtils.loadAnimation(requireContext(), R.anim.atg)
        val btgOne = AnimationUtils.loadAnimation(requireContext(), R.anim.btgone)
        val btgTwo = AnimationUtils.loadAnimation(requireContext(), R.anim.btgtwo)
        binding.swManagersAnimation.startAnimation(atg)
        binding.btnstart.startAnimation(btgOne)
        binding.stopwatchTaskApply.startAnimation(btgTwo)
        binding.stopwatchTaskEdit.startAnimation(btgOne)
        binding.btnstop.alpha = 0f
    }
}