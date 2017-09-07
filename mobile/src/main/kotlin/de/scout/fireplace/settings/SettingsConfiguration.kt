package de.scout.fireplace.settings

import de.scout.fireplace.Configuration
import javax.inject.Inject

internal class SettingsConfiguration @Inject constructor(private val configuration: Configuration) {

  fun isCriteriaEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_CRITERIA_ENABLED)
  }

  companion object {

    const val CONFIGURATION_CRITERIA_ENABLED = "criteria_enabled"
  }
}
