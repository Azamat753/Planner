package com.lawlett.planner.ui.main.timing

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
import androidx.lifecycle.ViewModelProvider
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.data.room.viewmodels.TimingViewModel
import com.lawlett.planner.extensions.gone
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.adapter.TimingAdapter
import com.lawlett.planner.utils.Const.Constants.CHANNEL_ID
import kotlinx.android.synthetic.main.fragment_create_tasks.*
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import org.koin.android.ext.android.inject

class StopwatchFragment : BaseFragment(R.layout.fragment_stopwatch) {
    var elapsedMillis: Long = 0
    var myTask: String? = null
    var roundingAlone: Animation? = null
    var atg: Animation? = null
    var btgOne: Animation? = null
    var btgTwo: Animation? = null
    var stopwatchTime: String? = null
    private var notificationManager: NotificationManagerCompat? = null
    private val viewModel by inject<TimingViewModel>()
    private val timingAdaper = TimingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationManager = NotificationManagerCompat.from(requireContext())
        initAnimation()
        initListeners()
//        insertToDataBase()
    }
//    private fun insertToDataBase() {
//        toolbar_title.text = getString(R.string.stopwatch)
//        viewModel.getData().observe(viewLifecycleOwner, { timings ->
//            timingAdaper.setData(timings)
//        })
//    }

    private fun initListeners() {
        stopwatch_task_apply.setOnClickListener {
            myTask = stopwatch_task_edit.text.toString()
            imageconst.gone()
            stopWatchConst.visible()
        }
        btnstart.setOnClickListener {
            showCustomNotification()
            icanchor_stopwatch.startAnimation(roundingAlone)
            btnstop.animate().alpha(1f).translationY(-80f).setDuration(300).start()
            btnstart.animate().alpha(0f).setDuration(300).start()
            btnstop.visible()
            btnstart.gone()
            timerHere!!.base = SystemClock.elapsedRealtime()
            timerHere!!.start()
        }
        btnstop.setOnClickListener {
            showElapsedTime()
            notificationManager?.cancel(1)
        }
    }

    private fun showElapsedTime() {
        elapsedMillis = SystemClock.elapsedRealtime() - timerHere!!.base
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
        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomBigContentView(expandedView)
            .setContentTitle(getString(R.string.stopwatch))
            .setContentText(getString(R.string.go_count))
            .setColor(getColor(requireContext(), R.color.myWhite))
            .build()

        notification.flags = Notification.FLAG_ONGOING_EVENT
        notificationManager!!.notify(1, notification)

    }
    private fun initAnimation() {
        atg = AnimationUtils.loadAnimation(requireContext(), R.anim.atg)
        btgOne = AnimationUtils.loadAnimation(requireContext(), R.anim.btgone)
        btgTwo = AnimationUtils.loadAnimation(requireContext(), R.anim.btgtwo)
        image_phone.startAnimation(atg)
        btnstart.startAnimation(btgOne)
        stopwatch_task_apply.startAnimation(btgTwo)
        stopwatch_task_edit.startAnimation(btgOne)
        btnstop.alpha = 0f
        roundingAlone = AnimationUtils.loadAnimation(requireContext(), R.anim.roundingalone)

    }

}