package de.scout.fireplace.feature.models

import android.support.annotation.Keep

@Keep
internal data class Search(
    val totalResults: Int,
    val pageSize: Int,
    val pageNumber: Int,
    val numberOfPages: Int,
    val numberOfListings: Int,
    val searchId: String,
    val results: List<Expose>
)
