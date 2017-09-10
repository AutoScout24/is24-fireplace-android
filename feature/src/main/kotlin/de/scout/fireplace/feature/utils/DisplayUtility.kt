package de.scout.fireplace.feature.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

internal object DisplayUtility {

  fun dp2px(context: Context, dp: Int): Int {
    val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics().apply { manager.defaultDisplay.getMetrics(this) }
    return (dp * metrics.density + 0.5f).toInt()
  }

  fun getScreenWidth(activity: Activity) = Point().apply { activity.windowManager.defaultDisplay.getSize(this) }.x
}
