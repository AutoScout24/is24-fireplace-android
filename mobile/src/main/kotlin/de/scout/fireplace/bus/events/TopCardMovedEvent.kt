package de.scout.fireplace.bus.events

import de.scout.fireplace.models.Expose

data class TopCardMovedEvent(val expose: Expose, val posX: Float)
