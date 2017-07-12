package de.scout.fireplace.home

import de.scout.fireplace.Configuration
import javax.inject.Inject

class HomeConfiguration @Inject constructor(private val configuration: Configuration) {

  fun isPreferencesEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_PREFERENCES_ENABLED)
  }

  fun isGalleryEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_GALLERY_ENABLED)
  }

  companion object {

    private val CONFIGURATION_PREFERENCES_ENABLED = "preferences_enabled"
    private val CONFIGURATION_GALLERY_ENABLED = "gallery_enabled"
  }
}
