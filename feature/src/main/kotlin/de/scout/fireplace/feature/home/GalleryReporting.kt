package de.scout.fireplace.feature.home

import android.os.Bundle
import de.scout.fireplace.feature.Reporting
import javax.inject.Inject

class GalleryReporting @Inject constructor(private val reporting: Reporting) {

  fun scroll(page: Int) = reporting.event(EVENT_SCROLL, Bundle().also { bundle -> bundle.putInt(PARAMETER_PAGE, page) })

  companion object {

    const val EVENT_SCROLL = "event_gallery_scroll"

    const val PARAMETER_PAGE = "parameter_gallery_page"
  }
}
