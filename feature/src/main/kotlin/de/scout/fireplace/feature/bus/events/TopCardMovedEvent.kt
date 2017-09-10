package de.scout.fireplace.feature.bus.events

import de.scout.fireplace.feature.models.Expose

internal data class TopCardMovedEvent(val expose: Expose, val posX: Float) : TopCardEvent
