package de.scout.fireplace.feature.settings

import android.content.SharedPreferences
import javax.inject.Inject

internal class RentSettingsRepository @Inject constructor(preferences: SharedPreferences) {

  var maxRentCold by IntPreference(preferences, "rent.max.rent.cold", 800)
  var minLivingSpace by IntPreference(preferences, "rent.min.living.space", 50)
  var minRooms by IntPreference(preferences, "rent.min.rooms", 2)

  var hasBalcony by BooleanPreference(preferences, "rent.criteria.balcony", false)
  var hasBasement by BooleanPreference(preferences, "rent.criteria.basement", false)
  var hasLift by BooleanPreference(preferences, "rent.criteria.lift", false)
}
