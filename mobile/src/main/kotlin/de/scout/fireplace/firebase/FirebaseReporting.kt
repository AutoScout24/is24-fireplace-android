package de.scout.fireplace.firebase

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import de.scout.fireplace.Reporting
import javax.inject.Inject

internal class FirebaseReporting @Inject constructor(application: Application) : Reporting {

  @SuppressLint("MissingPermission")
  private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

  override fun event(name: String) {
    analytics.logEvent(name, null)
    Log.d(TAG, name)
  }

  override fun event(name: String, bundle: Bundle) {
    analytics.logEvent(name, bundle)
    Log.d(TAG, "$name: $bundle")
  }

  companion object {

    const val TAG = "FIREBASE_REPORTING"
  }
}
