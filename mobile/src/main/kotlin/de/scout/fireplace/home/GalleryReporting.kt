package de.scout.fireplace.home

import android.os.Bundle
import de.scout.fireplace.Reporting
import javax.inject.Inject

class GalleryReporting @Inject constructor(private val reporting: Reporting) {

  fun reportScroll(page: Int) {
    reporting.event(EVENT_SCROLL, Bundle().also { bundle ->
      bundle.putInt(PARAMETER_PAGE, page)
    })
  }

  companion object {

    private val EVENT_SCROLL = "event.gallery.scroll"

    private val PARAMETER_PAGE = "event.gallery.page"
  }

}
