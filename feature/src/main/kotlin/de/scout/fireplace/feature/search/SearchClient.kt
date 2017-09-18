package de.scout.fireplace.feature.search

import android.location.Location
import de.scout.fireplace.feature.models.Search
import de.scout.fireplace.feature.network.SchedulingStrategy
import de.scout.fireplace.feature.settings.RentSettingsRepository
import de.scout.fireplace.feature.settings.SettingsConfiguration
import io.reactivex.Single
import java.util.HashMap
import javax.inject.Inject

internal class SearchClient @Inject constructor(
    private val service: SearchService,
    private val reporting: SearchReporting,
    private val configuration: SettingsConfiguration,
    private val settings: RentSettingsRepository,
    private val strategy: SchedulingStrategy
) {

  private var searchRadius = SEARCH_RADIUS_MINIMUM

  fun search(location: Location, type: String, page: Int): Single<Search> {
    val parameters = HashMap<String, String>()

    parameters.put("geocoordinates", getGeoCoordinates(location))
    parameters.put("sorting", SEARCH_SORTING_DISTANCE)

    if (type == "apartmentrent") {
      val apartmentTypes = getApartmentTypes()
      if (apartmentTypes.isNotEmpty()) parameters.put("apartmenttypes", apartmentTypes)

      val equipment = getEquipment()
      if (equipment.isNotEmpty()) parameters.put("equipment", equipment)
    }

    parameters.put("pagesize", "$SEARCH_PAGE_SIZE")
    parameters.put("pagenumber", page.toString())
    parameters.put("realestatetype", type)

    return service.search(SEARCH_TYPE_RADIUS, parameters).compose(strategy.single<Search>())
        .doOnSuccess { search -> reporting.search(search.pageNumber, searchRadius, search.totalResults) }
        .doOnSuccess { (_, _, pageNumber, numberOfPages, numberOfListings) ->
          if (numberOfListings == 0) {
            throw NoListingsException()
          }

          multiplySearchRadius(numberOfPages - pageNumber)
        }
  }

  private fun getGeoCoordinates(location: Location): String {
    return if (configuration.isCriteriaEnabled()) "${location.latitude};${location.longitude};${searchRadius}" else ""
  }

  private fun getApartmentTypes() = if (settings.hasBasement) "halfbasement" else ""

  private fun getEquipment(): String {
    val result = mutableListOf<String>()
    if (settings.hasBalcony) result.add("balcony")
    if (settings.hasLift) result.add("lift")
    return result.joinToString(",")
  }

  private fun multiplySearchRadius(buffer: Int) {
    searchRadius *= 1 + SEARCH_RADIUS_MULTIPLIER * Math.max(SEARCH_RESULTS_BUFFER - buffer, 0)
  }

  class NoListingsException : RuntimeException()

  companion object {

    private const val SEARCH_TYPE_RADIUS = "radius"
    private const val SEARCH_SORTING_DISTANCE = "distance"

    private const val SEARCH_RADIUS_MINIMUM = 1.0f
    private const val SEARCH_RADIUS_MULTIPLIER = 0.1f

    private const val SEARCH_PAGE_SIZE = 4
    private const val SEARCH_RESULTS_BUFFER = 5
  }
}
