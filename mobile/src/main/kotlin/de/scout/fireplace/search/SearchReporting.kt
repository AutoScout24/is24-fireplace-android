package de.scout.fireplace.search

import android.os.Bundle
import de.scout.fireplace.Reporting
import javax.inject.Inject

class SearchReporting @Inject constructor(private val reporting: Reporting) {

  fun reportSearch(page: Int, radius: Float, results: Int) {
    reporting.event(EVENT_SEARCH_REQUEST, Bundle().also { bundle ->
      bundle.putInt(PARAMETER_SEARCH_PAGE, page)
      bundle.putFloat(PARAMETER_SEARCH_RADIUS, radius)
      bundle.putInt(PARAMETER_SEARCH_RESULTS, results)
    })
  }

  companion object {

    private val EVENT_SEARCH_REQUEST = "event.search.request"

    private val PARAMETER_SEARCH_PAGE = "event.search.page"
    private val PARAMETER_SEARCH_RADIUS = "event.search.radius"
    private val PARAMETER_SEARCH_RESULTS = "event.search.results"
  }
}
