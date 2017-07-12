package de.scout.fireplace.home

import android.annotation.SuppressLint
import android.os.Bundle
import de.scout.fireplace.Reporting
import de.scout.fireplace.models.Expose
import javax.inject.Inject

@SuppressLint("NewApi")
class HomeReporting @Inject constructor(private val reporting: Reporting) {

  fun reportLike(expose: Expose, swiped: Boolean) {
    reporting.event(EVENT_LIKED, Bundle().apply {
      putString(PARAMETER_EXPOSE_ID, expose.id)
      putBoolean(PARAMETER_GESTURE_SWIPED, swiped)
    })
  }

  fun reportPass(expose: Expose, swiped: Boolean) {
    reporting.event(EVENT_PASSED, Bundle().apply {
      putString(PARAMETER_EXPOSE_ID, expose.id)
      putBoolean(PARAMETER_GESTURE_SWIPED, swiped)
    })
  }

  fun reportGallery(expose: Expose) {
    reporting.event(EVENT_GALLERY, Bundle().apply { putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  fun reportMatch(expose: Expose) {
    reporting.event(EVENT_MATCH, Bundle().apply { putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  companion object {

    private val EVENT_LIKED = "event.home.liked"
    private val EVENT_PASSED = "event.home.passed"
    private val EVENT_GALLERY = "event.home.gallery"
    private val EVENT_MATCH = "event.home.match"

    private val PARAMETER_EXPOSE_ID = "parameter.expose.id"
    private val PARAMETER_GESTURE_SWIPED = "parameter.gesture.swiped"
  }
}
