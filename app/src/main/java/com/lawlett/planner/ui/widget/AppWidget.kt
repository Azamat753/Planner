package com.lawlett.planner.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.lawlett.planner.R
import com.lawlett.planner.ui.main.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {

        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val buttonId = 100
        val views = RemoteViews(context.packageName, R.layout.app_widget)
        val intent = Intent(context, MainActivity::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
        intent.putExtra(BUTTON_KEY, buttonId)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, appWidgetId)
        views.setOnClickPendingIntent(R.id.widget_add_btn, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    companion object{
        var BUTTON_KEY:String = "button_id_key"

    }
}