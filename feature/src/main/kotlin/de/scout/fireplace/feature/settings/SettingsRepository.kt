package de.scout.fireplace.feature.settings

import android.content.SharedPreferences
import javax.inject.Inject

internal class SettingsRepository @Inject constructor(private val preferences: SharedPreferences) {

  var what by StringPreference(preferences, "what", "apartmentrent")

  var minimumPrice by IntPreference(preferences, "price.minimum", 200)
  var maximumPrice by IntPreference(preferences, "price.maximum", 2500)

  var minimumRooms by IntPreference(preferences, "rooms.minimum", 2)
  var maximumRooms by IntPreference(preferences, "rooms.maximum", 3)

  var minimumSpace by IntPreference(preferences, "space.minimum", 40)
  var maximumSpace by IntPreference(preferences, "space.maximum", 80)

  var hasBalcony by BooleanPreference(preferences, "criteria.balcony", false)
  var hasBasement by BooleanPreference(preferences, "criteria.basement", false)
  var hasLift by BooleanPreference(preferences, "criteria.lift", false)
}
