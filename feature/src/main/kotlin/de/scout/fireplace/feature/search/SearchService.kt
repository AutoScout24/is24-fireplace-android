package de.scout.fireplace.feature.search

import de.scout.fireplace.feature.models.Search
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

internal interface SearchService {

  @GET("/search")
  fun search(@Query("searchType") type: String, @QueryMap parameters: Map<String, String>): Single<Search>
}
