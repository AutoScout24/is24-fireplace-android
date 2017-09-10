package de.scout.fireplace.feature

internal interface Configuration {

  fun isEnabled(name: String): Boolean
}
