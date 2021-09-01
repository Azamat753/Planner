package com.lawlett.planner.ui.timing

import android.media.RingtoneManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.data.room.viewmodels.SkillViewModel
import com.lawlett.planner.databinding.FragmentTimerBinding
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.showToast
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.SimpleCountDownTimer
import org.koin.android.ext.android.inject
import java.lang.NullPointerException

class TimerFragment : BaseFragment<FragmentTimerBinding>(FragmentTimerBinding::inflate) {
    private var timeLeftInMilliseconds: Long = 0
    private lateinit var notificationManager: NotificationManagerCompat
    var myTask: String? = null
    var timeLeftText: String? = null
    var atg: Animation? = null
    var btgOne: Animation? = null
    var btgTwo: Animation? = null
    private val viewModel by inject<SkillViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        initNotification()
        initClickers()
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
                binding.applyButton.gone()
                binding.countTime.gone()
                binding.countdownText.text = getString(R.string.ready)
                binding.countdownButton.visible()
                binding.countdownText.visible()
                binding.mgAnimation.gone()
                binding.clockAnimation.visible()
            }
        }

        binding.countdownButton.setOnClickListener {
            startTimer()
            showNotification()
        }

        binding.exitButton.setOnClickListener {
            val myTime = binding.countdownText.text.toString()
            if (myTime.equals("0:00") || myTime.equals("0:01") || myTime.equals("0:02")) {
                val enterTime = (binding.countTime.text.toString().toDouble() / 60).toString()
                val model =
                    SkillModel(hour = enterTime, skillName = myTask, dateCreated = getTodayDate())
                viewModel.insertData(model)
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

    private fun showNotification() {
        val expandedView: RemoteViews = RemoteViews(
            requireContext().packageName,
            R.layout.notification_expanded_timer
        )

        object : SimpleCountDownTimer(timeLeftInMilliseconds, 1000) {
            override fun onTick(p0: Long) {
                super.onTick(p0)
                val minutes = p0.toInt() / 60000
                val seconds = p0.toInt() % 60000 / 1000

                timeLeftText = "" + minutes
                timeLeftText += ":"
                if (seconds < 10) timeLeftText += "0"
                timeLeftText += seconds

                expandedView.setTextViewText(R.id.timer_expanded, timeLeftText)

                val notification =
                    NotificationCompat.Builder(requireContext(), Constants.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_planner)
                        .setCustomBigContentView(expandedView)
                        .setContentTitle(getString(R.string.timer))
                        .setColor(getColor(requireContext(), R.color.textColor))
                        .setContentText(getString(R.string.go_count))
                        .setOnlyAlertOnce(true)
                        .build()

                notificationManager.notify(1, notification)

            }

            override fun onFinish() {
                super.onFinish()
                requireContext().showToast("00:00")
            }
        }.start()
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

    private fun initNotification() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        notificationManager = NotificationManagerCompat.from(requireContext())

    }
}