package de.scout.fireplace.feature.home

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerAppCompatDialogFragment
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.models.Expose
import kotlinx.android.synthetic.main.fragment_gallery.pager
import javax.inject.Inject

class GalleryFragment : DaggerAppCompatDialogFragment() {

  private var adapter: PagerAdapter? = null

  @Inject internal lateinit var reporting: GalleryReporting

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_gallery, container, false)
  }

  fun bind(pictures: List<Expose.Picture>) {
    this.adapter = GalleryAdapter(pictures, { position -> reporting.scroll(position) })
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    pager.adapter = adapter
  }
}
