package de.scout.fireplace.search;

import android.location.Location;
import de.scout.fireplace.models.Search;
import de.scout.fireplace.network.SchedulingStrategy;
import io.reactivex.Single;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class SearchClient {

  private static final String SEARCH_TYPE_RADIUS = "radius";
  private static final float SEARCH_RADIUS = 2.0F;

  private final SearchService service;
  private final SchedulingStrategy strategy;

  @Inject
  SearchClient(SearchService service, SchedulingStrategy strategy) {
    this.service = service;
    this.strategy = strategy;
  }

  public Single<Search> search(Location location, int page, int size) {
    Map<String, String> parameters = new HashMap<>();

    parameters.put("geocoordinates", getGeoCoordinates(location));
    parameters.put("sorting", "standard");

    //parameters.put("features", "adKeysAndStringValues,viareporting,virtualTour");
    parameters.put("pagesize", String.valueOf(size));
    parameters.put("pagenumber", String.valueOf(page));

    parameters.put("realestatetype", "apartmentrent");
    //parameters.put("publishedAfter", "2017-07-11T14:12:02");

    return service.search(SEARCH_TYPE_RADIUS, parameters)
        .compose(strategy.single());
  }

  private String getGeoCoordinates(Location location) {
    return String.format("%s;%s;%s", location.getLatitude(), location.getLongitude(), SEARCH_RADIUS);
  }
}
