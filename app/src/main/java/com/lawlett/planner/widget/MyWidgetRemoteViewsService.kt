package com.lawlett.planner.widget

import android.content.Intent
import android.widget.RemoteViewsService

class MyWidgetRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return MyWidgetRemoteViewsFactory(applicationContext, p0!!)
    }
}