package de.scout.fireplace.feature.home

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.models.Expose

internal class GalleryAdapter internal constructor(private val pictures: List<Expose.Picture>, private val callback: (Int) -> Unit) : PagerAdapter() {

  override fun getCount() = pictures.size

  override fun isViewFromObject(view: View, obj: Any) = view === obj

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val view = LayoutInflater.from(container.context).inflate(R.layout.item_gallery, container, false)
    val parent = container.parent as View

    Picasso.with(view.context)
        .load(pictures[position].getUrl(parent.width, parent.height))
        .fit()
        .centerCrop()
        .into(view.findViewById<ImageView>(R.id.image))

    callback.invoke(position)
    container.addView(view)
    return view
  }

  override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
    container.removeView(item as View)
  }
}
