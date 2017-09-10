package de.scout.fireplace.feature.bus.events

import de.scout.fireplace.feature.models.Expose

internal data class TopCardPressEvent(val expose: Expose) : TopCardEvent
