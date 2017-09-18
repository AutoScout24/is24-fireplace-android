package de.scout.fireplace.feature.home

import de.scout.fireplace.feature.models.Expose
import de.scout.fireplace.feature.settings.BuySettingsRepository
import de.scout.fireplace.feature.settings.RentSettingsRepository
import de.scout.fireplace.feature.ui.FloatingCardStackEvent
import javax.inject.Inject

internal class EventMatcher @Inject constructor(
    private val rentRepository: RentSettingsRepository,
    private val buyRepository: BuySettingsRepository
) {

  fun match(event: FloatingCardStackEvent, type: String) = event.type === FloatingCardStackEvent.Type.LIKE && matches(event.expose, type)

  private fun matches(expose: Expose, type: String): Boolean {
    val attributes = expose.attributes
    if (attributes.size != 3) {
      return false
    }

    if (type == "apartmentrent") return when {
      attributes.parse(0) > rentRepository.maxRentCold -> false
      attributes.parse(1) < rentRepository.minLivingSpace -> false
      attributes.parse(2) < rentRepository.minRooms -> false
      else -> true
    }

    if (type == "apartmentbuy") return when {
      attributes.parse(0) > buyRepository.maxRentCold -> false
      attributes.parse(1) < buyRepository.minLivingSpace -> false
      attributes.parse(2) < buyRepository.minRooms -> false
      else -> true
    }

    return false
  }

  private fun List<Expose.Attribute>.parse(index: Int) = get(index).value.toNumeric().toFloat()

  private fun String.toNumeric() = replace("[^\\d]".toRegex(), "")
}
