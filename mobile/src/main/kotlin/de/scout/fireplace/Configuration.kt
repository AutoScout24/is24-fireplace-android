package de.scout.fireplace

interface Configuration {

  fun isEnabled(name: String): Boolean

  fun getValue(name: String): String
}
