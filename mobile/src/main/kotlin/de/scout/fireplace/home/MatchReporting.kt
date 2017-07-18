package de.scout.fireplace.home

import android.os.Bundle
import de.scout.fireplace.Reporting
import de.scout.fireplace.models.Expose
import javax.inject.Inject

internal class MatchReporting @Inject constructor(private val reporting: Reporting) {

  fun ignore(expose: Expose) {
    reporting.event(EVENT_CONTINUE, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  fun details(expose: Expose) {
    reporting.event(EVENT_DETAILS, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  companion object {

    const val EVENT_CONTINUE = "event_match_continue"
    const val EVENT_DETAILS = "event_match_details"

    const val PARAMETER_EXPOSE_ID = "parameter_expose_id"
  }
}
