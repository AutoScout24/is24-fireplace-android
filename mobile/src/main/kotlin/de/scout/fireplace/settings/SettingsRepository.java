package de.scout.fireplace.settings;

import android.content.SharedPreferences;
import javax.inject.Inject;

public class SettingsRepository {

  private static final String KEY_PREF_PRICE_MIN = "KEY_PREF_PRICE_MIN";
  private static final String KEY_PREF_PRICE_MAX = "KEY_PREF_PRICE_MAX";

  private static final String KEY_PREF_BALCONY = "KEY_PREF_BALCONY";
  private static final String KEY_PREF_KITCHEN = "KEY_PREF_KITCHEN";

  private static final String KEY_PREF_NUMBER_ROOMS = "KEY_PREF_NUMBER_ROOMS";

  private final SharedPreferences preferences;

  @Inject
  SettingsRepository(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  public int getMinPrice() {
    return preferences.getInt(KEY_PREF_PRICE_MIN, 200);
  }

  public void setMinPrice(int minPrice) {
    preferences.edit().putInt(KEY_PREF_PRICE_MIN, minPrice).apply();
  }

  public int getMaxPrice() {
    return preferences.getInt(KEY_PREF_PRICE_MAX, 2500);
  }

  public void setMaxPrice(int maxPrice) {
    preferences.edit().putInt(KEY_PREF_PRICE_MAX, maxPrice).apply();
  }

  public boolean hasBalcony() {
    return preferences.getBoolean(KEY_PREF_BALCONY, false);
  }

  public void hasBalcony(boolean balcony) {
    preferences.edit().putBoolean(KEY_PREF_BALCONY, balcony).apply();
  }

  public boolean hasKitchen() {
    return preferences.getBoolean(KEY_PREF_KITCHEN, false);
  }

  public void hasKitchen(boolean kitchen) {
    preferences.edit().putBoolean(KEY_PREF_KITCHEN, kitchen).apply();
  }

  public int getNumberRooms() {
    return preferences.getInt(KEY_PREF_NUMBER_ROOMS, 2);
  }

  public void setMinRooms(int minRooms) {
    preferences.edit().putInt(KEY_PREF_NUMBER_ROOMS, minRooms).apply();
  }
}
