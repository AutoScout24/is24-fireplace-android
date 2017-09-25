package de.scout.fireplace.feature.settings

import android.content.SharedPreferences
import javax.inject.Inject

internal class BuySettingsRepository @Inject constructor(preferences: SharedPreferences) {

  var maxRentCold by IntPreference(preferences, "buy.max.rent.cold", 800)
  var minLivingSpace by IntPreference(preferences, "buy.min.living.space", 50)
  var minRooms by IntPreference(preferences, "buy.min.rooms", 2)
}
