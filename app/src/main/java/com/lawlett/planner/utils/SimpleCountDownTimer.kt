package com.lawlett.planner.utils

import android.os.CountDownTimer

open class SimpleCountDownTimer(
    millisInFuture: Long,
    countDownInterval: Long
) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(p0: Long) {

    }

    override fun onFinish() {

    }
}