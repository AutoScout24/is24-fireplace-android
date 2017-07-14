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

    const val EVENT_SCROLL = "event.gallery.scroll"

    const val PARAMETER_PAGE = "event.gallery.page"
  }

}
