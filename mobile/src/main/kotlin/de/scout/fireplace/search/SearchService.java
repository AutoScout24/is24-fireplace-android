package de.scout.fireplace.search;

import de.scout.fireplace.models.Search;
import io.reactivex.Single;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface SearchService {

  @GET("/home/search")
  Single<Search> search(@Query("searchType") String type, @QueryMap Map<String, String> parameters);
}
