package de.scout.fireplace.feature.ui

import de.scout.fireplace.feature.models.Expose

internal data class FloatingCardStackEvent(val expose: Expose, val type: Type, val count: Int) {

  enum class Type {
    LIKE,
    PASS
  }
}
