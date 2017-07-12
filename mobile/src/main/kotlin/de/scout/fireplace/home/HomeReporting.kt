package de.scout.fireplace.home

import android.os.Bundle
import de.scout.fireplace.Reporting
import de.scout.fireplace.models.Expose
import javax.inject.Inject

class HomeReporting @Inject constructor(private val reporting: Reporting) {

  fun reportLike(expose: Expose, swiped: Boolean) {
    reporting.event(EVENT_LIKED, Bundle().also { bundle ->
      bundle.putString(PARAMETER_EXPOSE_ID, expose.id)
      bundle.putBoolean(PARAMETER_GESTURE_SWIPED, swiped)
    })
  }

  fun reportPass(expose: Expose, swiped: Boolean) {
    reporting.event(EVENT_PASSED, Bundle().also { bundle ->
      bundle.putString(PARAMETER_EXPOSE_ID, expose.id)
      bundle.putBoolean(PARAMETER_GESTURE_SWIPED, swiped)
    })
  }

  fun reportGallery(expose: Expose) {
    reporting.event(EVENT_GALLERY, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  fun reportMatch(expose: Expose) {
    reporting.event(EVENT_MATCH, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  fun reportPreferences() {
    reporting.event(EVENT_PREFERENCES)
  }

  companion object {

    private val EVENT_LIKED = "event.home.liked"
    private val EVENT_PASSED = "event.home.passed"
    private val EVENT_GALLERY = "event.home.gallery"
    private val EVENT_MATCH = "event.home.match"

    private val EVENT_PREFERENCES = "event.home.preferences"

    private val PARAMETER_EXPOSE_ID = "parameter.expose.id"
    private val PARAMETER_GESTURE_SWIPED = "parameter.gesture.swiped"
  }
}
