package de.scout.fireplace.settings;

import android.content.SharedPreferences;
import javax.inject.Inject;

public class SettingsRepository {

  private static final String PRICE_MINIMUM = "price.minimum";
  private static final String PRICE_MAXIMUM = "price.maximum";

  private static final String SPACE_MINIMUM = "space.minimum";
  private static final String SPACE_MAXIMUM = "space.maximum";

  private static final String ROOMS_MINIMUM = "rooms.minimum";
  private static final String ROOMS_MAXIMUM = "rooms.maximum";

  private static final String KEY_PREF_BALCONY = "KEY_PREF_BALCONY";
  private static final String KEY_PREF_KITCHEN = "KEY_PREF_KITCHEN";
  private static final String KEY_PREF_GARAGE = "KEY_PREF_GARAGE";
  private static final String KEY_PREF_CELLAR = "KEY_PREF_CELLAR";
  private static final String KEY_PREF_LIFT = "KEY_PREF_LIFT";
  private static final String KEY_PREF_GARDEN = "KEY_PREF_GARDEN";
  private static final String KEY_PREF_NEW_BUILD = "KEY_PREF_NEW_BUILD";

  private final SharedPreferences preferences;

  @Inject
  SettingsRepository(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  public int getMinPrice() {
    return preferences.getInt(PRICE_MINIMUM, 200);
  }

  void setMinPrice(int minPrice) {
    preferences.edit().putInt(PRICE_MINIMUM, minPrice).apply();
  }

  public int getMaxPrice() {
    return preferences.getInt(PRICE_MAXIMUM, 2500);
  }

  void setMaxPrice(int maxPrice) {
    preferences.edit().putInt(PRICE_MAXIMUM, maxPrice).apply();
  }

  public int getMinRooms() {
    return preferences.getInt(ROOMS_MINIMUM, 2);
  }

  void setMinRooms(int minRooms) {
    preferences.edit().putInt(ROOMS_MINIMUM, minRooms).apply();
  }

  int getMaxRooms() {
    return preferences.getInt(ROOMS_MAXIMUM, 3);
  }

  void setMaxRooms(int maxRooms) {
    preferences.edit().putInt(ROOMS_MINIMUM, maxRooms).apply();
  }

  int getMinSpace() {
    return preferences.getInt(SPACE_MINIMUM, 40);
  }

  void setMinSpace(int minSpace) {
    preferences.edit().putInt(SPACE_MINIMUM, minSpace).apply();
  }

  int getMaxSpace() {
    return preferences.getInt(SPACE_MAXIMUM, 80);
  }

  void setMaxSpace(int maxSpace) {
    preferences.edit().putInt(SPACE_MAXIMUM, maxSpace).apply();
  }

  boolean hasBalcony() {
    return preferences.getBoolean(KEY_PREF_BALCONY, false);
  }

  void hasBalcony(boolean balcony) {
    preferences.edit().putBoolean(KEY_PREF_BALCONY, balcony).apply();
  }

  boolean hasKitchen() {
    return preferences.getBoolean(KEY_PREF_KITCHEN, false);
  }

  void hasKitchen(boolean kitchen) {
    preferences.edit().putBoolean(KEY_PREF_KITCHEN, kitchen).apply();
  }

  void hasGarage(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_GARAGE, checked).apply();
  }

  void hasCellar(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_CELLAR, checked).apply();
  }

  void hasLift(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_LIFT, checked).apply();
  }

  void hasGarden(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_GARDEN, checked).apply();
  }

  void isNewBuild(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_NEW_BUILD, checked).apply();
  }
}
