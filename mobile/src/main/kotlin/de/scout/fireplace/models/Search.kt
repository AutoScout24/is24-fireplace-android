package de.scout.fireplace.models

data class Search(
    val totalResults: Int,
    val pageSize: Int,
    val pageNumber: Int,
    val numberOfPages: Int,
    val numberOfListings: Int,
    val searchId: String,
    val results: List<Expose>
)
