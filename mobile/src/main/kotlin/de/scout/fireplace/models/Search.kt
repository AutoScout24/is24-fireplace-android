package de.scout.fireplace.models

data class Search(val totalResults: Int, val pageSize: Int, val pageNumber: Int, val numberOfPages: Int, val results: List<Result>) {

  data class Result(val isNew: Boolean, val radius: Boolean, val infoLine: String, val id: String, val attributes: List<String>, val pictureUrl: String?)
}
