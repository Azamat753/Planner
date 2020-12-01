package com.lawlett.planner.ui.main.timing

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.toastShow
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.utils.Const.Constants.CHANNEL_ID
import com.lawlett.planner.utils.SimpleCountDownTimer
import kotlinx.android.synthetic.main.fragment_timer.*

class TimerFragment : BaseFragment(R.layout.fragment_timer) {
    lateinit var mp: MediaPlayer
    private var timeLeftInMilliseconds: Long = 0
    lateinit var countDownTimer: CountDownTimer
    private lateinit var notificationManager: NotificationManagerCompat
    var myTask: String? = null
    var timeLeftText: String? = null
    var roundingAlone: Animation? = null
    var atg: Animation? = null
    var btgOne: Animation? = null
    var btgTwo: Animation? = null

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
        roundingAlone = AnimationUtils.loadAnimation(requireContext(), R.anim.roundingalone)
    }

    private fun initClickers() {
        close_button.setOnClickListener {
            mp.reset()
            notificationManager.cancel(1)
            if (countDownTimer != null)
                findNavController().navigateUp()
        }

        timer_task_apply.setOnClickListener {
            if (timer_task_edit.text.toString().isEmpty()) {
                requireContext().toastShow(getString(R.string.empty))
            } else {
                myTask = timer_task_edit.text.toString()
                image_const.gone()
                timerConst.visible()
            }
        }
        apply_button.setOnClickListener {
            if (editText.text.toString().equals("") || editText.text.toString().toInt() < 1) {
                requireContext().toastShow(getString(R.string.zero_minutes_pass))

            } else {
                timeLeftInMilliseconds = (editText.text.toString().toInt() * 60000).toLong()
                timeLeftText = timeLeftInMilliseconds.toString()
                apply_button.gone()
                editText.gone()
                countdown_text.text = getString(R.string.ready)
                countdown_button.visible()
                countdown_text.visible()
            }
        }
        countdown_button.setOnClickListener {
            startTimer()
            showNotification()
        }
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

                val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCustomBigContentView(expandedView)
                    .setContentTitle(getString(R.string.timer))
                    .setColor(getColor(requireContext(), R.color.myWhite))
                    .setContentText(getString(R.string.go_count))
                    .setOnlyAlertOnce(true)
                    .build()

                notificationManager.notify(1, notification)

            }

            override fun onFinish() {
                super.onFinish()
                requireContext().toastShow("00:00")
            }
        }.start()
    }

    private fun startTimer() {
        icanchor.startAnimation(roundingAlone)
        countdown_button.animate().alpha(0f).setDuration(300).start()
        object : SimpleCountDownTimer(timeLeftInMilliseconds, 1000) {
            override fun onTick(p0: Long) {
                super.onTick(p0)
                var minutes: Int = p0.toInt() / 60000
                var seconds: Int = p0.toInt() % 60000 / 1000

                timeLeftText = "" + minutes
                timeLeftText += ":"
                if (seconds < 10) timeLeftText += ""
                timeLeftText += seconds

                countdown_text.text = timeLeftText
                countdown_button.gone()
                exit_button.visible()

            }

            override fun onFinish() {
                super.onFinish()
                if (mp != null) {
                    mp.start()
                } else {
                    requireContext().toastShow(getString(R.string.timer_end))
                }
                icanchor.clearAnimation()
                countdown_button.visible()
            }
        }.start()
    }

    fun lockBack() {

    }
    private fun initNotification() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mp = MediaPlayer.create(requireContext(), notification)
        notificationManager = NotificationManagerCompat.from(requireContext())

    }
}