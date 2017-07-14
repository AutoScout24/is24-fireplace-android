package de.scout.fireplace.home

import android.os.Bundle
import de.scout.fireplace.Reporting
import de.scout.fireplace.models.Expose
import javax.inject.Inject

class MatchReporting @Inject constructor(private val reporting: Reporting) {

  fun reportContinue(expose: Expose) {
    reporting.event(EVENT_CONTINUE, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  fun reportDetails(expose: Expose) {
    reporting.event(EVENT_DETAILS, Bundle().also { bundle -> bundle.putString(PARAMETER_EXPOSE_ID, expose.id) })
  }

  companion object {

    const val EVENT_CONTINUE = "event.match.continue"
    const val EVENT_DETAILS = "event.match.details"

    const val PARAMETER_EXPOSE_ID = "parameter.expose.id"
  }
}
