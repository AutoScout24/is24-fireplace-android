package de.scout.fireplace.home;

import de.scout.fireplace.models.Expose;
import de.scout.fireplace.preference.PreferenceRepository;
import de.scout.fireplace.ui.FloatingCardStackEvent;
import javax.inject.Inject;

class EventMatcher {

  private static final String SPLITTER = "\\|";

  private final PreferenceRepository repository;

  @Inject
  EventMatcher(PreferenceRepository repository) {
    this.repository = repository;
  }

  boolean match(FloatingCardStackEvent event) {
    return event.getType() == FloatingCardStackEvent.Type.APPROVED && matches(event.getSummary());
  }

  private boolean matches(Expose.Summary summary) {
    String[] attributes = summary.getDescription().split(SPLITTER);
    if (attributes.length != 3) {
      return false;
    }

    float price = parseFloat(attributes[0]);
    if (price < repository.getMinPrice() || price > repository.getMaxPrice()) {
      return false;
    }

    int rooms = parseInt(attributes[2]);
    if (rooms < repository.getMinRooms()) {
      return false;
    }

    return true;
  }

  private float parseFloat(String string) {
    return Float.parseFloat(string.replaceAll("[^\\d]", ""));
  }

  private int parseInt(String string) {
    return Integer.parseInt(string.replaceAll("[^\\d]", ""));
  }
}
