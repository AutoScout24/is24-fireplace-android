package de.scout.fireplace.bus.events

import de.scout.fireplace.models.Expose

internal data class TopCardLongPressEvent(val expose: Expose) : TopCardEvent
