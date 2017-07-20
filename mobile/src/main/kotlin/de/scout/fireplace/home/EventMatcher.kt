package de.scout.fireplace.home

import de.scout.fireplace.models.Expose
import de.scout.fireplace.settings.SettingsRepository
import de.scout.fireplace.ui.FloatingCardStackEvent
import javax.inject.Inject

internal class EventMatcher @Inject constructor(private val repository: SettingsRepository) {

  fun match(event: FloatingCardStackEvent): Boolean {
    return event.type === FloatingCardStackEvent.Type.LIKE && matches(event.expose)
  }

  private fun matches(expose: Expose): Boolean {
    val attributes = expose.attributes
    if (attributes.size != 3) {
      return false
    }

    val price = attributes[0].value.toNumeric().toFloat()
    if (price < repository.minPrice || price > repository.maxPrice) {
      return false
    }

    val rooms = attributes[2].value.toNumeric().toInt()
    if (rooms < repository.minRooms) {
      return false
    }

    return true
  }

  private fun String.toNumeric(): String {
    return replace("[^\\d]".toRegex(), "")
  }
}
