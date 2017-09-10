package de.scout.fireplace.feature

interface Configuration {

  fun isEnabled(name: String): Boolean
}
