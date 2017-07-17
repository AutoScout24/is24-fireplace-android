package de.scout.fireplace.search

import android.location.Location
import de.scout.fireplace.R
import de.scout.fireplace.models.Search
import de.scout.fireplace.network.SchedulingStrategy
import de.scout.fireplace.network.StringResException
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class SearchClient @Inject internal constructor(private val service: SearchService, private val reporting: SearchReporting, private val strategy: SchedulingStrategy) {

  private var searchRadius = SEARCH_RADIUS_MINIMUM

  fun search(location: Location, page: Int, size: Int): Single<Search> {
    val parameters = HashMap<String, String>()

    parameters.put("geocoordinates", getGeoCoordinates(location))
    parameters.put("sorting", SEARCH_SORTING_DISTANCE)

    //parameters.put("features", "adKeysAndStringValues,viareporting,virtualTour");
    parameters.put("pagesize", size.toString())
    parameters.put("pagenumber", page.toString())

    parameters.put("realestatetype", "apartmentrent")
    //parameters.put("publishedAfter", "2017-07-11T14:12:02");

    return service.search(SEARCH_TYPE_RADIUS, parameters).compose(strategy.single<Search>())
        .doOnSuccess { search -> reporting.search(search.pageNumber, searchRadius, search.totalResults) }
        .doOnSuccess { search ->
          if (search.numberOfListings == 0) {
            throw StringResException(R.string.error_listings_unavailable)
          }

          multiplySearchRadius(search.numberOfPages - search.pageNumber)
        }
  }

  private fun multiplySearchRadius(buffer: Int) {
    searchRadius *= 1 + SEARCH_RADIUS_MULTIPLIER * Math.max(SEARCH_RESULTS_BUFFER - buffer, 0)
  }

  private fun getGeoCoordinates(location: Location): String {
    return String.format("%s;%s;%s", location.latitude, location.longitude, searchRadius)
  }

  companion object {

    const val SEARCH_TYPE_RADIUS = "radius"
    const val SEARCH_SORTING_DISTANCE = "distance"

    const val SEARCH_RADIUS_MINIMUM = 1.0f
    const val SEARCH_RADIUS_MULTIPLIER = 0.1f

    const val SEARCH_RESULTS_BUFFER = 5
  }
}
