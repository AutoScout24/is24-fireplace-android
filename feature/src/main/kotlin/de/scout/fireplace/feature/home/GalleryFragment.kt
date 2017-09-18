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

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    pager.adapter = adapter
  }

  fun bind(pictures: List<Expose.Picture>) {
    this.adapter = GalleryAdapter(pictures, { position -> reporting.scroll(position) })
  }
}
