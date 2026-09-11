package io.github.invertium.bmsdash

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.github.invertium.bmsdash.widget.BmsWidgetProvider

class MainActivity : FlutterActivity() {
    private val channel = "io.github.invertium.bmsdash/widget"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channel)
            .setMethodCallHandler { call, result ->
                if (call.method == "updateWidget") {
                    val soc = call.argument<Int>("soc")
                    val voltage = call.argument<Double>("voltage")
                    val current = call.argument<Double>("current")
                    val power = call.argument<Double>("power")

                    val prefs = getSharedPreferences("bms_widget", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putInt("soc", soc ?: -1)
                        .putFloat("voltage", voltage?.toFloat() ?: Float.NaN)
                        .putFloat("current", current?.toFloat() ?: Float.NaN)
                        .putFloat("power", power?.toFloat() ?: Float.NaN)
                        .apply()

                    val manager = AppWidgetManager.getInstance(this)
                    val component = ComponentName(this, BmsWidgetProvider::class.java)
                    BmsWidgetProvider.updateAll(this, manager, component)

                    result.success(null)
                } else {
                    result.notImplemented()
                }
            }
    }
}
