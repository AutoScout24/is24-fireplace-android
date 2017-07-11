package de.scout.fireplace.models

import java.lang.StringBuilder

data class Search(
    val totalResults: Int,
    val pageSize: Int,
    val pageNumber: Int,
    val numberOfPages: Int,
    val numberOfListings: Int,
    val searchId: String,
    val results: List<Result>
) {

  data class Result(
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

    data class Picture(private val urlScaleAndCrop: String, val type: String?) {

      fun url(width: Int, height: Int): String {
        return StringBuilder(urlScaleAndCrop)
            .replace("%WIDTH%", width.toString())
            .replace("%HEIGHT%", height.toString())
            .toString()
      }

      private fun StringBuilder.replace(from: String, to: String): StringBuilder {
        return replace(indexOf(from), indexOf(from) + from.length, to)
      }
    }

    data class Address(val distance: String, val line: String)

    data class Attribute(val label: String, val value: String) {
      override fun toString(): String {
        return value
      }
    }

    data class Realtor(val logoUrlScale: String, val showcasePlacementColor: String?)

    enum class ListingType { S, M, L, XL }
  }
}
