package com.lawlett.planner.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.lawlett.planner.data.room.database.MainDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyWidgetRemoteViewsFactory(private val context: Context, val intent: Intent) :
    RemoteViewsService.RemoteViewsFactory {
    private var mCount = 0
    private val mWidgetItems: MutableList<WidgetItem> = ArrayList()
    private var mAppWidgetId = 0

    val EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM"

    override fun onCreate() {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            MainDataBase.getDatabase(context).taskDao().loadCategoryLiveData("Talk")
                .observeForever {
                    it?.let {
                        mCount = it.size
                        for (element in it) {
                            mWidgetItems.add(WidgetItem(element.task))
                        }
                    }
                    Log.e("gogogo", "onCreate: $it")
                }
        }
        mAppWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )

        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear()
    }

    override fun getCount(): Int {
        return mCount
    }

    override fun getViewAt(position: Int): RemoteViews {

        val rv = RemoteViews(context.packageName, com.lawlett.planner.R.layout.widget_item)
        rv.setTextViewText(com.lawlett.planner.R.id.widget_item, mWidgetItems[position].text)

        val extras = Bundle()
        extras.putInt(EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(com.lawlett.planner.R.id.widget_item, fillInIntent)
        Log.e("qwqwqw", "onUpdate: getViewat" )
        try {
            println("Loading view $position")
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        // Return the remote views object.
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}