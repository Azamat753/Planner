package com.lawlett.planner.ui.timing

import android.app.Notification
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getColor
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.TimingModel
import com.lawlett.planner.data.room.viewmodels.TimingViewModel
import com.lawlett.planner.databinding.FragmentStopwatchBinding
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class StopwatchFragment :
    BaseFragment<FragmentStopwatchBinding>(FragmentStopwatchBinding::inflate) {
    var elapsedMillis: Long = 0
    var myTask: String? = null
    var roundingAlone: Animation? = null
    var atg: Animation? = null
    var btgOne: Animation? = null
    var btgTwo: Animation? = null
    var stopwatchTime: String? = null
    private var notificationManager: NotificationManagerCompat? = null
    private val viewModel by inject<TimingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationManager = NotificationManagerCompat.from(requireContext())
        initAnimation()
        initListeners()
        insertToDataBase()
    }

    private fun insertToDataBase() {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val monthName = arrayOf(
            getString(R.string.january),
            getString(R.string.february),
            getString(R.string.march),
            getString(R.string.april),
            getString(R.string.may),
            getString(R.string.june),
            getString(R.string.july),
            getString(R.string.august),
            getString(R.string.september),
            getString(R.string.october),
            getString(R.string.november),
            getString(R.string.december)
        )
        val month = monthName[c[Calendar.MONTH]]
        val currentDate = SimpleDateFormat("dd ", Locale.getDefault()).format(Date())

//        binding.toolbarTitle.text = getString(R.string.stopwatch)
        var stopwatch = TimingModel(
            stopwatch = binding.stopwatchTaskEdit.text.toString(),
            stopwatchDay = "$currentDate $month $year"
        )
        viewModel.addTask(stopwatch)
    }


    private fun initListeners() {
        binding.stopwatchTaskApply.setOnClickListener {
            myTask = binding.stopwatchTaskEdit.text.toString()
            binding.imageconst.gone()
            binding.stopWatchConst.visible()
        }
        binding.btnstart.setOnClickListener {
            showCustomNotification()
            binding.icanchorStopwatch.startAnimation(roundingAlone)
            binding.btnstop.animate().alpha(1f).translationY(-80f).setDuration(300).start()
            binding.btnstart.animate().alpha(0f).setDuration(300).start()
            binding.btnstop.visible()
            binding.btnstart.gone()
            binding.timerHere!!.base = SystemClock.elapsedRealtime()
            binding.timerHere!!.start()
        }
        binding.btnstop.setOnClickListener {
            showElapsedTime()
            notificationManager?.cancel(1)

        }
    }

    private fun showElapsedTime() {
        elapsedMillis = SystemClock.elapsedRealtime() - binding.timerHere!!.base
        stopwatchTime = (elapsedMillis / 60000).toString()
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
        atg = AnimationUtils.loadAnimation(requireContext(), R.anim.atg)
        btgOne = AnimationUtils.loadAnimation(requireContext(), R.anim.btgone)
        btgTwo = AnimationUtils.loadAnimation(requireContext(), R.anim.btgtwo)
        binding.imagePhone.startAnimation(atg)
        binding.btnstart.startAnimation(btgOne)
        binding.stopwatchTaskApply.startAnimation(btgTwo)
        binding.stopwatchTaskEdit.startAnimation(btgOne)
        binding.btnstop.alpha = 0f
        roundingAlone = AnimationUtils.loadAnimation(requireContext(), R.anim.roundingalone)

    }

}