package de.scout.fireplace.home

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import butterknife.BindView
import de.scout.fireplace.R
import de.scout.fireplace.activity.AbstractFragment
import de.scout.fireplace.models.Expose
import javax.inject.Inject

class GalleryFragment : AbstractFragment() {

  private var adapter: PagerAdapter? = null

  @BindView(R.id.pager) internal lateinit var pager: ViewPager

  @Inject internal lateinit var reporting: GalleryReporting

  override fun getLayoutId(): Int {
    return R.layout.fragment_gallery
  }

  fun bind(pictures: List<Expose.Picture>) {
    this.adapter = GalleryAdapter(pictures, { position -> reporting.scroll(position) })
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    pager.adapter = adapter
  }
}
