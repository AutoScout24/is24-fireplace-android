package de.scout.fireplace.feature.home

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.View
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.activity.AbstractFragment
import de.scout.fireplace.feature.models.Expose
import kotlinx.android.synthetic.main.fragment_gallery.pager
import javax.inject.Inject

class GalleryFragment : AbstractFragment() {

  private var adapter: PagerAdapter? = null

  @Inject internal lateinit var reporting: GalleryReporting

  override fun getLayoutId() = R.layout.fragment_gallery

  internal fun bind(pictures: List<Expose.Picture>) {
    adapter = GalleryAdapter(pictures, { position -> reporting.scroll(position) })
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    pager.adapter = adapter
  }
}
