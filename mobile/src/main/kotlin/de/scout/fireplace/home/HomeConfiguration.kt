package de.scout.fireplace.home

import de.scout.fireplace.Configuration
import javax.inject.Inject

class HomeConfiguration @Inject constructor(private val configuration: Configuration) {

  fun isSettingsEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_SETTINGS_ENABLED)
  }

  fun isGalleryEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_GALLERY_ENABLED)
  }

  companion object {

    const val CONFIGURATION_SETTINGS_ENABLED = "settings_enabled"
    const val CONFIGURATION_GALLERY_ENABLED = "gallery_enabled"
  }
}
