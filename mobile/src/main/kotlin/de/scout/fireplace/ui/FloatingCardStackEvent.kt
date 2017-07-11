package de.scout.fireplace.ui

import de.scout.fireplace.models.Expose

data class FloatingCardStackEvent(val summary: Expose.Summary, val type: Type, val count: Int) {

  enum class Type {
    APPROVED,
    DISMISSED
  }
}
