package de.scout.fireplace.home

import android.os.Bundle
import de.scout.fireplace.feature.Reporting
import de.scout.fireplace.models.Expose
import javax.inject.Inject

internal class HomeReporting @Inject constructor(private val reporting: Reporting) {

  fun like(expose: Expose) = reporting.event(EVENT_LIKED, Bundle().apply { putString(PARAMETER_EXPOSE_ID, expose.id) })

  fun pass(expose: Expose) = reporting.event(EVENT_PASSED, Bundle().apply { putString(PARAMETER_EXPOSE_ID, expose.id) })

  fun manual() = reporting.event(EVENT_MANUAL)

  fun gallery(expose: Expose) = reporting.event(EVENT_GALLERY, Bundle().apply { putString(PARAMETER_EXPOSE_ID, expose.id) })

  fun match(expose: Expose) = reporting.event(EVENT_MATCH, Bundle().apply { putString(PARAMETER_EXPOSE_ID, expose.id) })

  fun settings() = reporting.event(EVENT_SETTINGS)

  companion object {

    const val EVENT_LIKED = "event_home_liked"
    const val EVENT_PASSED = "event_home_passed"
    const val EVENT_MANUAL = "event_home_manual"

    const val EVENT_GALLERY = "event_home_gallery"
    const val EVENT_MATCH = "event_home_match"

    const val EVENT_SETTINGS = "event_home_settings"

    const val PARAMETER_EXPOSE_ID = "parameter_expose_id"
  }
}
