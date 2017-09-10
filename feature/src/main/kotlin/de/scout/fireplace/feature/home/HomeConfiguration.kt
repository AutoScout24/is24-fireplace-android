package de.scout.fireplace.feature.home

import de.scout.fireplace.feature.Configuration
import javax.inject.Inject

internal class HomeConfiguration @Inject constructor(private val configuration: Configuration) {

  fun isSettingsEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_SETTINGS_ENABLED)
  }

  fun isGalleryEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_GALLERY_ENABLED)
  }

  fun isShortcutEnabled(): Boolean {
    return configuration.isEnabled(CONFIGURATION_SHORTCUT_ENABLED)
  }

  companion object {

    const val CONFIGURATION_SETTINGS_ENABLED = "settings_enabled"
    const val CONFIGURATION_GALLERY_ENABLED = "gallery_enabled"
    const val CONFIGURATION_SHORTCUT_ENABLED = "shortcut_enabled"
  }
}
