package de.scout.fireplace.feature.home

import de.scout.fireplace.feature.models.Expose
import de.scout.fireplace.feature.settings.SettingsRepository
import de.scout.fireplace.feature.ui.FloatingCardStackEvent
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
    if (price !in repository.minimumPrice..repository.maximumPrice) {
      return false
    }

    val rooms = attributes[2].value.toNumeric().toInt()
    if (rooms !in repository.minimumRooms..repository.maximumRooms) {
      return false
    }

    return true
  }

  private fun String.toNumeric() = replace("[^\\d]".toRegex(), "")
}
