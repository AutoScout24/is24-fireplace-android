package de.scout.fireplace.firebase

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import de.scout.fireplace.Reporting
import javax.inject.Inject

internal class FirebaseReporting @Inject constructor(application: Application) : Reporting {

  @SuppressLint("MissingPermission")
  private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

  override fun event(message: String, bundle: Bundle) {
    analytics.logEvent(message, bundle)
  }
}
