package de.scout.fireplace.home

import android.os.Bundle
import de.scout.fireplace.Reporting
import de.scout.fireplace.models.Expose
import javax.inject.Inject

class HomeReporting @Inject constructor(private val reporting: Reporting) {

  fun reportLike(expose: Expose) {
    reporting.event(EVENT_LIKED, Bundle().also { bundle ->
      bundle.putString(PARAMETER_EXPOSE_ID, expose.id)
    })
  }

  fun reportPass(expose: Expose) {
    reporting.event(EVENT_PASSED, Bundle().also { bundle ->
      bundle.putString(PARAMETER_EXPOSE_ID, expose.id)
    })
  }

  fun reportManual() {
    reporting.event(EVENT_MANUAL)
  }

  fun reportGallery(expose: Expose) {
    reporting.event(EVENT_GALLERY, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  fun reportMatch(expose: Expose) {
    reporting.event(EVENT_MATCH, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  fun reportSettings() {
    reporting.event(EVENT_SETTINGS)
  }

  companion object {

    const val EVENT_LIKED = "event.home.liked"
    const val EVENT_PASSED = "event.home.passed"
    const val EVENT_MANUAL = "event.home.manual"

    const val EVENT_GALLERY = "event.home.gallery"
    const val EVENT_MATCH = "event.home.match"

    const val EVENT_SETTINGS = "event.home.settings"

    const val PARAMETER_EXPOSE_ID = "parameter.expose.id"
  }
}
