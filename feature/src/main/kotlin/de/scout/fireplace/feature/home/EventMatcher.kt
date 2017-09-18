package de.scout.fireplace.feature.home

import de.scout.fireplace.feature.models.Expose
import de.scout.fireplace.feature.settings.RentSettingsRepository
import de.scout.fireplace.feature.ui.FloatingCardStackEvent
import javax.inject.Inject

internal class EventMatcher @Inject constructor(private val repository: RentSettingsRepository) {

  fun match(event: FloatingCardStackEvent) = event.type === FloatingCardStackEvent.Type.LIKE && matches(event.expose)

  private fun matches(expose: Expose): Boolean {
    val attributes = expose.attributes
    if (attributes.size != 3) {
      return false
    }

    val price = attributes[0].value.toNumeric().toFloat()
    if (price > repository.maxRentCold) {
      return false
    }

    val space = attributes[1].value.toNumeric().toInt()
    if (space < repository.minLivingSpace) {
      return false
    }

    val rooms = attributes[2].value.toNumeric().toInt()
    if (rooms < repository.minRooms) {
      return false
    }

    return true
  }

  private fun String.toNumeric() = replace("[^\\d]".toRegex(), "")
}
