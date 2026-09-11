package io.github.invertium.bmsdash.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import io.github.invertium.bmsdash.gabriel.R

class BmsWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    companion object {
        fun updateAll(
            context: Context,
            manager: AppWidgetManager,
            component: ComponentName
        ) {
            updateWidgets(context, manager, manager.getAppWidgetIds(component))
        }

        private fun updateWidgets(
            context: Context,
            manager: AppWidgetManager,
            ids: IntArray
        ) {
            val prefs = context.getSharedPreferences("bms_widget", Context.MODE_PRIVATE)
            val soc = prefs.getInt("soc", -1)
            val voltage = prefs.getFloat("voltage", Float.NaN)
            val current = prefs.getFloat("current", Float.NaN)
            val power = prefs.getFloat("power", Float.NaN)

            for (id in ids) {
                val views = RemoteViews(context.packageName, R.layout.bms_widget)

                views.setTextViewText(
                    R.id.widget_soc,
                    if (soc >= 0) "$soc %" else "-- %"
                )
                views.setTextViewText(
                    R.id.widget_voltage,
                    if (!voltage.isNaN()) "%.2f V".format(voltage) else "-- V"
                )
                views.setTextViewText(
                    R.id.widget_current,
                    if (!current.isNaN()) "%.2f A".format(current) else "-- A"
                )
                views.setTextViewText(
                    R.id.widget_power,
                    if (!power.isNaN()) "%.0f W".format(power) else "-- W"
                )

                manager.updateAppWidget(id, views)
            }
        }
    }
}
