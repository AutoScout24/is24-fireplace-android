package de.scout.fireplace.feature.settings

import android.content.SharedPreferences
import javax.inject.Inject

internal class RentSettingsRepository @Inject constructor(preferences: SharedPreferences) {

  var maxRentCold by IntPreference(preferences, "max.rent.cold", 800)
  var minLivingSpace by IntPreference(preferences, "min.living.space", 50)
  var minRooms by IntPreference(preferences, "min.rooms", 2)

  var hasBalcony by BooleanPreference(preferences, "criteria.balcony", false)
  var hasBasement by BooleanPreference(preferences, "criteria.basement", false)
  var hasLift by BooleanPreference(preferences, "criteria.lift", false)
}
