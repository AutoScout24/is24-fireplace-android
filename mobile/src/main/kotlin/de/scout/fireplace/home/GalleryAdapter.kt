package de.scout.fireplace.home

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import de.scout.fireplace.R
import de.scout.fireplace.models.Expose

class GalleryAdapter internal constructor(private val pictures: List<Expose.Picture>, private val callback: (Int) -> Unit) : PagerAdapter() {

  override fun getCount(): Int {
    return pictures.size
  }

  override fun isViewFromObject(view: View, obj: Any): Boolean {
    return view === obj
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val view = LayoutInflater.from(container.context).inflate(R.layout.item_gallery, container, false)
    val parent = container.parent as View

    Picasso.with(view.context)
        .load(pictures.first().getUrl(parent.width, parent.height))
        .fit()
        .centerCrop()
        .into(ButterKnife.findById<ImageView>(view, R.id.image))

    callback.invoke(position)
    container.addView(view)
    return view
  }

  private fun <T> List<T>.first(): T {
    return get(0)
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    container.removeView(`object` as View)
  }
}
