package de.scout.fireplace.home

import android.support.v4.app.FragmentActivity
import javax.inject.Inject

internal class Navigator @Inject constructor(private val activity: FragmentActivity) {

  fun navigate(function: (FragmentActivity) -> Unit) = function(activity)
}
