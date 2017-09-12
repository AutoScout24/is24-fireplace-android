package de.scout.fireplace.feature.search

import android.os.Bundle
import de.scout.fireplace.feature.Reporting
import javax.inject.Inject

class SearchReporting @Inject constructor(private val reporting: Reporting) {

  fun search(page: Int, radius: Float, results: Int) {
    reporting.event(EVENT_SEARCH_REQUEST, Bundle().apply {
      putInt(PARAMETER_SEARCH_PAGE, page)
      putFloat(PARAMETER_SEARCH_RADIUS, radius)
      putInt(PARAMETER_SEARCH_RESULTS, results)
    })
  }

  companion object {

    const val EVENT_SEARCH_REQUEST = "event_search_request"

    const val PARAMETER_SEARCH_PAGE = "parameter_search_page"
    const val PARAMETER_SEARCH_RADIUS = "parameter_search_radius"
    const val PARAMETER_SEARCH_RESULTS = "parameter_search_results"
  }
}
