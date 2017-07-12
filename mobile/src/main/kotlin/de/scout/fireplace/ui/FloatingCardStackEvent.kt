package de.scout.fireplace.ui

import de.scout.fireplace.models.Expose

data class FloatingCardStackEvent(val expose: Expose, val type: Type, val count: Int) {

  enum class Type {
    APPROVED,
    DISMISSED
  }
}
