package de.scout.fireplace.bus.events

import de.scout.fireplace.models.Expose

internal data class TopCardMovedEvent(val expose: Expose, val posX: Float) : TopCardEvent
