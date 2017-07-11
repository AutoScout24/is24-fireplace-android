package de.scout.fireplace.search

import de.scout.fireplace.models.Search
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SearchService {

  @GET("/search")
  fun search(@Query("searchType") type: String, @QueryMap parameters: Map<String, String>): Single<Search>
}
