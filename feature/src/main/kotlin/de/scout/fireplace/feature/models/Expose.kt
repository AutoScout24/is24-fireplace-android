package de.scout.fireplace.feature.models

import android.support.annotation.Keep
import android.view.View
import java.lang.StringBuilder

@Keep
internal data class Expose(
    val id: String,
    val pictures: List<Picture>,
    val address: Address,
    val isProject: Boolean,
    val isPrivate: Boolean,
    val listingType: ListingType,
    val published: String,
    val isNewObject: Boolean,
    val attributes: List<Attribute>,
    val realtor: Realtor
) {

  fun getPictureFor(view: View): String {
    if (pictures.isEmpty()) {
      throw IndexOutOfBoundsException("No pictures available")
    }

    return pictures[0].getUrl(view.width, view.height)
  }

  @Keep
  data class Picture(private val urlScaleAndCrop: String, val type: String?) {

    fun getUrl(width: Int, height: Int): String {
      return StringBuilder(urlScaleAndCrop)
          .replace("%WIDTH%", width.toString())
          .replace("%HEIGHT%", height.toString())
          .toString()
    }

    private fun StringBuilder.replace(from: String, to: String): StringBuilder {
      return replace(indexOf(from), indexOf(from) + from.length, to)
    }
  }

  @Keep
  data class Address(val distance: String, val line: String)

  @Keep
  data class Attribute(val label: String, val value: String) {
    override fun toString(): String {
      return value
    }
  }

  @Keep
  data class Realtor(val logoUrlScale: String, val showcasePlacementColor: String?)

  @Keep
  enum class ListingType { S, M, L, XL }
}
